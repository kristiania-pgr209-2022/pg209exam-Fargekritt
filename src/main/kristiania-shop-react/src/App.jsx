import {useEffect, useState} from 'react'
import './App.css'
import {HashRouter, Link, Route, Routes, useNavigate} from "react-router-dom";

function ListItem() {
    const [loading, setLoading] = useState(true);
    const [item, setItem] = useState([]);


    useEffect(() => {
        const fetchData = async () => {
            const data = await fetch("/api/items");
            setItem(await data.json());
            setLoading(false);
        }
        fetchData()
            .catch(console.error)
    }, []);


    if (loading) {
        return <div>Loading...</div>
    }
    return <div>
        <Link to={"/"}>Back to home</Link>
        <div>
            {item.map((i) => (
                <ul>
                    <li>{i.name}</li>
                    <li>{i.artNumber}</li>
                    <li>{i.category}</li>
                    <li>{i.description}</li>
                </ul>
            ))}
        </div>
    </div>
}


function AddItem() {
    const navigate = useNavigate()
    const [name, setName] = useState("");
    const [artNumber, setArtNumber] = useState("");
    const [category, setCategory] = useState("");
    const [description, setDescription] = useState("");

    async function handleSubmit(e) {

        e.preventDefault();
        await fetch("/api/items", {
            method: "post",
            body: JSON.stringify({name, artNumber, category, description}),
            headers: {
                "Content-Type": "application/json"
            }
        });

        navigate("/items")
    }

    return (
        <div className="App">
            <h1>Add item</h1>
            <Link to={"/"}>Back to home</Link>
            <form onSubmit={handleSubmit}>
                <div><label>Name: <input type="text" onChange={e => setName(e.target.value)}/></label></div>
                <div><label>Art number: <input type="text" onChange={e => setArtNumber(e.target.value)}/></label></div>
                <div><label>Category: <input type="text" onChange={e => setCategory(e.target.value)}/></label></div>
                <div><label>Description: <textarea onChange={e => setDescription(e.target.value)}></textarea></label>
                </div>
                <button>Submit</button>
            </form>
        </div>
    )
}


function FrontPage() {

    return <div>
        <h1>FRONT PAGE OF DOOM!</h1>
        <ul>
            <li><Link to={"/items"}>List items</Link></li>
            <li><Link to={"/items/add"}>Add a new item</Link></li>
        </ul>
    </div>;
}

function App() {

    return <HashRouter>
        <Routes>
            <Route path={"/"} element={<FrontPage/>}></Route>
            <Route path={"/items/add"} element={<AddItem/>}></Route>
            <Route path={"/items"} element={<ListItem/>}></Route>
        </Routes>
    </HashRouter>
}


export default App