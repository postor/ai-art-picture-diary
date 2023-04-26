import { Fragment } from "react"
import { NavLink } from "react-router-dom"

const categories = ['javascript', 'html']
const titles = ['my-blog', 'some-article']

export default function Index() {
  return <div>
    <h1>Posts</h1>
    <ul>
      {categories.map((x, i) => <Fragment key={i}>{titles.map((y, j) => <li key={j}>
        <NavLink to={`/posts/${x}/${y}`}>{`${x}/${y}`}</NavLink>
      </li>)}</Fragment>)}
    </ul>
  </div>
}