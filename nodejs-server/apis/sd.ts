import axios from 'axios'
import uniqid from 'uniqid'
import { ChatGPTAPI } from 'chatgpt'
import { connect } from "nats"

const { SD_URL = 'http://localhost:7860', OPENAI_API_KEY, NATS = '127.0.0.1:4222' } = process.env

const nc = connect({ servers: NATS })

const api = new ChatGPTAPI({
  apiKey: OPENAI_API_KEY
})
let lastMessageId = ""

const textEncoder = new TextEncoder()

export const drawImage = async (prompt: string, base64 = "") => {
  if (base64) {
    prompt = base64ToStr(base64)
  }

  let res = await (await nc).request('draw', textEncoder.encode(prompt),{ timeout: 20000 })
  
  return {
    image: Buffer.from(res.data).toString('base64'),
    prompt,
  }
}



export const drawImageFromIdea = async (idea: string, base64 = "") => {
  if (base64) {
    idea = base64ToStr(base64)
  }
  console.log({ idea })
  const prompt = await getStableDiffusionPrompt(idea)
  return drawImage(prompt)
}


function base64ToStr(base64 = '') {
  let buff = Buffer.from(base64, 'base64')
  return buff.toString('utf-8');
}

export const getStableDiffusionPrompt = async (idea: string) => {
  if (lastMessageId) {
    try {
      let prompt = getInitialPrompt(idea)
      let res = await api.sendMessage(prompt, { parentMessageId: lastMessageId })
      lastMessageId = res.id
      return res.text
    } catch (e) { }
  }
  let prompt = getInitialPrompt(idea)
  let res = await api.sendMessage(prompt)
  lastMessageId = res.id
  return res.text
}

function getInitialPrompt(idea: string) {
  return `Stable Diffusion is an AI art generation model similar to DALLE-2.
Here are some prompts for generating art with Stable Diffusion. 

Example:

- portait of a homer simpson archer shooting arrow at forest monster, front game card, drark, marvel comics, dark, intricate, highly detailed, smooth, artstation, digital illustration
- pirate, concept art, deep focus, fantasy, intricate, highly detailed, digital painting, artstation, matte, sharp focus, illustration
- ghost inside a hunted room, art by lois van baarle and loish and ross tran and rossdraws and sam yang and samdoesarts and artgerm, digital art, highly detailed, intricate, sharp focus, Trending on Artstation HQ, deviantart, unreal engine 5, 4K UHD image
- red dead redemption 2, cinematic view, epic sky, detailed, concept art, low angle, high detail, warm lighting, volumetric, godrays, vivid, beautiful, trending on artstation
- a fantasy style portrait painting of rachel lane / alison brie hybrid in the style of francois boucher oil painting unreal 5 daz. rpg portrait, extremely detailed artgerm
- athena, greek goddess, claudia black, art by artgerm and greg rutkowski and magali villeneuve, bronze greek armor, owl crown, d & d, fantasy, intricate, portrait, highly detailed, headshot, digital painting, trending on artstation, concept art, sharp focus, illustration
- closeup portrait shot of a large strong female biomechanic woman in a scenic scifi environment, intricate, elegant, highly detailed, centered, digital painting, artstation, concept art, smooth, sharp focus, warframe, illustration
- ultra realistic illustration of steve urkle as the hulk, intricate, elegant, highly detailed, digital painting, artstation, concept art, smooth, sharp focus, illustration
- portrait of beautiful happy young ana de armas, ethereal, realistic anime, trending on pixiv, detailed, clean lines, sharp lines, crisp lines, award winning illustration, masterpiece, 4k, eugene de blaas and ross tran, vibrant color scheme, intricately detailed
- A highly detailed and hyper realistic portrait of a gorgeous young ana de armas, lisa frank, trending on artstation, butterflies, floral, sharp focus, studio photo, intricate details, highly detailed, alberto seveso and geo2099 style

Prompts should be written in English, excluding the artist name, and include the following rule:

- Follow the structure of the example prompts. This means Write a description of the scene, followed by modifiers divided by commas to alter the mood, style, lighting, and more, excluding the artist name, separated by commas. place a extra commas.
- Always use style cartoon, and artist Sandra Boynton
- the word "I" in IDEA is a 5 year old girl
- total letters within 120 letters

I want you to write me a detailed prompt exactly about the IDEA and follow the rule, if the IDEA is not enough to generate prompt apply open field to the idea
  
IDEA: ${idea}

answer no explain, no idea, nothing else, only the prompt content:`
}
