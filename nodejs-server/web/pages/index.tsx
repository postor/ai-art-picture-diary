import { useEffect, useState } from "react"
import { hello } from "../../apis/hello"
import { drawImage, drawImageFromIdea } from "../../apis/sd"

export default function Index() {
  let [greeting, setGreeting] = useState('loading...')
  useEffect(() => {
    hello('world').then(x => setGreeting(x))
  }, [setGreeting])
  let [idea, setIdea] = useState('今天下雨')
  let [res, setRes] = useState(null)
  let [loading, setLoading] = useState(false)
  return <div>
    <h1>Index</h1>
    <p>{greeting}</p>
    <input value={idea} onChange={e => setIdea(e.target.value)} disabled={loading} />
    <button disabled={loading} onClick={async () => {
      setLoading(true)
      let res = await drawImage(idea)
      setRes(res)
      setIdea('')
      setLoading(false)
    }}>提交</button>
    <img src={res ? "data:image/png;base64," + res.image : "about:blank"} />
    <pre>{JSON.stringify(res, null, 2)}</pre>
  </div>
}