import {useEffect, useState} from 'react'
import './App.css'
import {HashRouter, Link, Route, Routes, useNavigate} from "react-router-dom";

function ListUser({userId}) {
    const navigate = useNavigate();
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

    function handleClick(i) {
        userId(i);
        navigate("/threads")
    }

    return <div style={{width: '50vw', height: '50vh'}}>
        <h1>Log in with a user</h1>
        <Link to={"/"}>Back to home</Link>
        <div>
            {user.map((i) => (
                <button value={i} onClick={() => handleClick(i)} style={{border: '1px solid black'}}>
                    <dl>
                        <dt>UserName:</dt>
                        <dd>- {i.username}</dd>
                        <dt>FirstName:</dt>
                        <dd>- {i.firstName}</dd>
                        <dt>LastName:</dt>
                        <dd>- {i.lastName}</dd>
                    </dl>
                </button>
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
        <h1>CHAT ROOM OF DOOM!</h1>
        <ul>
            <li><Link to={"/users"}>List users</Link></li>
            <li><Link to={"/users/add"}>Add a new user</Link></li>
        </ul>
    </div>;
}

function ListMessages() {
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState([]);


    useEffect(() => {
        const fetchData = async () => {
            const data = await fetch("/api/messages/thread/1");
            setMessage(await data.json());
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
            {message.map((i) => (
                <ul>
                    <li>{i.user.username}</li>
                    <li>{i.body}</li>
                    <li>{i.sentDate}</li>
                </ul>
            ))}
        </div>
    </div>

}

function ListThreads({user}) {
   console.log(user)
    const [loading, setLoading] = useState(true);
    const [thread, setThread] = useState([]);


    useEffect(() => {
        const fetchData = async () => {
            const data = await fetch("/api/users/" + user.id + "/threads");
            setThread(await data.json());
            setLoading(false);
        }
        fetchData()
            .catch(console.error)
    }, []);

    if (loading) {
        return <div>Loading...</div>
    }

    return <div>
        <h1>Threads your a member of</h1>
        <Link to={"/"}>Back to home</Link>
        <div>
            {thread.map((i) => (
                <button style={{border: '1px solid black'}}>
                    <dl>
                        <dt>ThreadID:</dt>
                        <dd>- {i.id}</dd>
                        <dt>Creator:</dt>
                        <dd>- {i.creator.username}</dd>
                        <dt>Subject:</dt>
                        <dd>- {'Subject'}</dd>
                    </dl>
                </button>
            ))}
        </div>
    </div>

}

function AddThread() {
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

function AddMessage() {
    return null;
}

function App() {
    const [user, setUser] = useState();

    return <HashRouter>
        <Routes>
            <Route path={"/"} element={<FrontPage/>}></Route>
            <Route path={"/users"} element={<ListUser userId={setUser}/>}></Route>
            <Route path={"/users/add"} element={<AddUser/>}></Route>
            <Route path={"/threads"} element={<ListThreads user={user}/>}></Route>
            <Route path={"/threads/add"} element={<AddThread/>}></Route>
            <Route path={"/messages"} element={<ListMessages/>}></Route>
            <Route path={"/messages/add"} element={<AddMessage/>}></Route>

        </Routes>
    </HashRouter>
}


export default App