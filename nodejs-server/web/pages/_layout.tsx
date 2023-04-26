import Nav from "../components/Nav";
import { Outlet } from 'react-router-dom'

export default function Layout() {
  return <>
    <header>
      <Nav />
    </header>
    <main>
      <Outlet />
    </main>
    <footer>
      some footer here
    </footer>
  </>
}