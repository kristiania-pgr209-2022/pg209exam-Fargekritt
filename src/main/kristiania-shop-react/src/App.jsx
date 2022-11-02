import {useEffect, useState} from 'react'
import './App.css'
import {HashRouter, Link, Route, Routes, useNavigate} from "react-router-dom";

function ListUser() {
    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState([]);


    useEffect(() => {
        const fetchData = async () => {
            const data = await fetch("/api/users");
            setUser(await data.json());
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
            {user.map((i) => (
                <ul>
                    <li>{i.firstName}</li>
                    <li>{i.lastName}</li>
                    <li>{i.username}</li>
                    <li>{i.gender}</li>
                </ul>
            ))}
        </div>
    </div>
}


function AddUser() {
    const navigate = useNavigate()
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [gender, setGender] = useState("");

    async function handleSubmit(e) {

        e.preventDefault();
        await fetch("/api/users", {
            method: "post",
            body: JSON.stringify({firstName, lastName, gender, username}),
            headers: {
                "Content-Type": "application/json"
            }
        });

        navigate("/users")
    }

    return (
        <div className="App">
            <h1>Add item</h1>
            <Link to={"/"}>Back to home</Link>
            <form onSubmit={handleSubmit}>
                <div><label>First Name: <input type="text" onChange={e => setFirstName(e.target.value)}/></label></div>
                <div><label>Last Name: <input type="text" onChange={e => setLastName(e.target.value)}/></label></div>
                <div><label>Username: <input type="text" onChange={e => setUsername(e.target.value)}/></label></div>
                <div><label>Gender: <textarea onChange={e => setGender(e.target.value)}></textarea></label>
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
            <li><Link to={"/users"}>List items</Link></li>
            <li><Link to={"/users/add"}>Add a new item</Link></li>
        </ul>
    </div>;
}

function App() {

    return <HashRouter>
        <Routes>
            <Route path={"/"} element={<FrontPage/>}></Route>
            <Route path={"/users/add"} element={<AddUser/>}></Route>
            <Route path={"/users"} element={<ListUser/>}></Route>
        </Routes>
    </HashRouter>
}



export default App