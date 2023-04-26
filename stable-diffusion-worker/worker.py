from diffusers import StableDiffusionPipeline, EulerDiscreteScheduler
import torch

import asyncio
import nats
import io

nega_prompt = "bad anatomy,text,bad proportions,blurry,cloned face,cropped,deformed,dehydrated,disfigured,duplicate,error,extra arms,extra fingers,extra legs,extra limbs,fused fingers,gross proportions,jpeg artifacts,long neck,low quality,lowres,malformed limbs,missing arms,missing legs,morbid,mutated hands,mutation,mutilated,out of frame,poorly drawn face,poorly drawn hands,signature,too many fingers,ugly,username,watermark,worst quality"

async def main():


    model_id = "stabilityai/stable-diffusion-2"

    scheduler = EulerDiscreteScheduler.from_pretrained(model_id, subfolder="scheduler")
    pipe = StableDiffusionPipeline.from_pretrained(model_id, scheduler=scheduler, torch_dtype=torch.float16)
    pipe = pipe.to("cuda")



    async with (await nats.connect('nats://127.0.0.1:4222')) as nc:
        print(f'Connected to NATS at {nc.connected_url.netloc}...')
        subscribe = await nc.subscribe('draw')
        async for msg in subscribe.messages:
          prompt = msg.data.decode('utf-8')
          print(prompt)
          image = pipe(prompt, negative_prompt=nega_prompt, width=800, height=480, num_inference_steps=20).images[0]

          img_byte_arr = io.BytesIO()
          image.save(img_byte_arr, format='PNG')
          await nc.publish(msg.reply, img_byte_arr.getvalue())


if __name__ == '__main__':
    loop = asyncio.get_event_loop()
    loop.run_until_complete(main())
    loop.run_forever()
    loop.close()

