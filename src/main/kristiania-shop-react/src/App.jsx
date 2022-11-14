import {useEffect, useState} from 'react'
import './App.css'
import {HashRouter, Link, Route, Routes, useNavigate} from "react-router-dom";
import Select from "react-select";


//=============================================LIST=============================================================
function ListUser({setSelectedUser}) {

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

    function handleClick(user) {
        setSelectedUser(user);
        navigate("/threads")
    }

    return <div style={{width: '50vw', height: '50vh'}}>
        <h1>Log in with a user</h1>
        <Link to={"/"}>Back to home</Link>
        <div>
            {user.map((u) => (
                <button onClick={() => handleClick(u)} style={{border: '1px solid black'}}>
                    <dl>
                        <dt>UserName:</dt>
                        <dd>- {u.username}</dd>
                        <dt>FirstName:</dt>
                        <dd>- {u.firstName}</dd>
                        <dt>LastName:</dt>
                        <dd>- {u.lastName}</dd>
                    </dl>
                </button>
            ))}
        </div>
    </div>
}


function ListThreads({user, setSelectedThread}) {

    const navigate = useNavigate();
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

    function handleClick(thread) {
        setSelectedThread(thread);
        navigate("/messages")
    }

    return <div>
        <h1>Threads your a member of</h1>
        <Link to={"/"}>Back to home</Link>
        <div>
            {thread.map((t) => (
                <button value={t} onClick={() => handleClick(t)} style={{border: '1px solid black'}}>
                    <dl>
                        <dt>ThreadID:</dt>
                        <dd>- {t.id}</dd>
                        <dt>Creator:</dt>
                        <dd>- {t.creator.username}</dd>
                        <dt>Subject:</dt>
                        <dd>- {'Subject'}</dd>
                    </dl>
                </button>
            ))}
        </div>
        <Link to={"/threads/add"}>Start a chat</Link>
    </div>
}


function ListMessages({thread}) {

    const [loading, setLoading] = useState(true);
    const [loading2, setLoading2] = useState(true);
    const [message, setMessage] = useState([]);
    const [members, setMembers] = useState([]);

    useEffect(() => {
        const fetchMessage = async () => {
            const data = await fetch("/api/messages/thread/" + thread.id);
            setMessage(await data.json());
            setLoading(false);
        }
        const fetchThreadMembers = async () => {
            const data = await fetch("/api/thread/" + thread.id + "/members");
            setMembers(await data.json())
            setLoading2(false)
        }
        fetchThreadMembers()
            .catch(console.error)
        fetchMessage()
            .catch(console.error)
    }, []);


    if (loading || loading2) {
        return <div>Loading...</div>
    }

    return <div>
        <Link to={"/"}>Back to home</Link>
        <div style={{border: '2px solid black'}}>
            <h2>ThreadMembers:</h2>
            {members.map((i) => (
                <p>{i.username}</p>
            ))}
        </div>
        <div>
            {message.map((i) => (
                <div style={{border: '1px solid black'}}>
                    <dl>
                        <dt>User:</dt>
                        <dd>- {i.user.username}</dd>
                        <dt>Message:</dt>
                        <dd>{i.body}</dd>
                        <dt>Date sent:</dt>
                        <dd>{i.sentDate}</dd>
                    </dl>
                </div>
            ))}
        </div>
        <Link to={"/messages/add"}>Add message</Link>
        <Link to={"/threads/members/add"}>Add a member to the chat</Link>
    </div>
}

//=============================================LIST END=============================================================

//=============================================ADD=============================================================

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

function AddThread({creator}) {
    const navigate = useNavigate()
    const [thread, setThread] = useState();
    const [title, setTitle] = useState();
    const [receiverId, setReceiverId] = useState();
    const [message, setMessage] = useState();


    async function handleSubmit(e) {

        e.preventDefault();

        const request = {
            method: "post",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({creator, receiverId, title, message})
        }
        await fetch("/api/thread/", request)

        navigate("/users/")
    }

    return (
        <div className="App">
            <h1>Start Chat</h1>
            <Link to={"/"}>Back to home</Link>
            <form onSubmit={handleSubmit}>
                <div><label>Title: <input type="text" onChange={e => setTitle(e.target.value)}/></label></div>
                <div><label>Receiver: <input type="text" onChange={e => setReceiverId(e.target.value)}/></label></div>
                <div><label>Message: <textarea onChange={e => setMessage(e.target.value)}></textarea></label>
                </div>
                <button>Submit</button>
            </form>
        </div>
    )
}

function AddMessage({user, thread}) {
    const navigate = useNavigate()
    const [body, setBody] = useState();

    async function handleSubmit(e) {

        e.preventDefault();

        const request = {
            method: "post",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({body, user, thread})
        }
        await fetch("/api/messages", request)

        navigate("/messages")
    }

    return (
        <div className="App">
            <h1>Create message</h1>
            <Link to={"/"}>Back to home</Link>
            <form onSubmit={handleSubmit}>
                <div><label>Message: <textarea onChange={e => setBody(e.target.value)}></textarea></label>
                </div>
                <button>Submit</button>
            </form>
        </div>)
}

function AddMember({thread}) {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [users, setUsers] = useState([]);
    const [user, setUser] = useState();

    useEffect(() => {
        const fetchData = async () => {
            const data = await fetch("/api/users");
            setUsers(await data.json());
            setLoading(false);
        }
        fetchData()
            .catch(console.error)
    }, []);
    if (loading) {
        return <div>Loading...</div>

    }

    async function handleSubmit(e) {

        console.log(user)
        e.preventDefault()


        const request = {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({user, thread})
        }
        await fetch("/api/thread/member", request);

        navigate("/messages")
    }

    const handleSelectChange = (event) => {
        console.log(event.target.value)
        setUser(event)
    }
console.log(thread)
    console.log(user)

  /*  return (<Select name="users"
                    options={users}
                    value={user}
                    onChange={setUser}
                    getOptionLabel={(option)=>option.username}
                    getOptionValue={(option)=>option.id}
    />)*/
    return (
        <div className="App">
            <h1>Add member to thread</h1>
            <Link to={"/"}>Back to home</Link>
            <form onSubmit={handleSubmit}>
                <label>Choose a member:</label>

                <Select name="users"
                        options={users}
                        value={user}
                        onChange={setUser}
                        getOptionLabel={(option)=>option.username}
                        getOptionValue={(option)=>option.id}
                />
             {/*   <select value={user} onChange={handleSelectChange}>

                    <option>Choose a user</option>
                    {users.map((user, index) => {
                        return <option key={index}>{user.username}</option>
                    })}

                    {users.map((u) => (
                           <option key={u} value={u}>{u.username}</option>
                       ))}
                </select>*/}
                <button>Add Member</button>
            </form>
        </div>
    )
}


//=============================================ADD END=============================================================

function FrontPage() {

    return <div>
        <h1>CHAT ROOM OF DOOM!</h1>
        <ul>
            <li><Link to={"/users"}>List users</Link></li>
            <li><Link to={"/users/add"}>Add a new user</Link></li>
        </ul>
    </div>;
}


function App() {
    const [user, setUser] = useState();
    const [thread, setThread] = useState();

    return <HashRouter>
        <Routes>
            <Route path={"/"} element={<FrontPage/>}></Route>
            <Route path={"/users"} element={<ListUser setSelectedUser={setUser}/>}></Route>
            <Route path={"/users/add"} element={<AddUser/>}></Route>
            <Route path={"/threads"} element={<ListThreads user={user} setSelectedThread={setThread}/>}></Route>
            <Route path={"/threads/add"} element={<AddThread creator={user}/>}></Route>
            <Route path={"/messages"} element={<ListMessages thread={thread}/>}></Route>
            <Route path={"/messages/add"} element={<AddMessage user={user} thread={thread}/>}></Route>
            <Route path={"threads/members/add"} element={<AddMember thread={thread}/>}></Route>

        </Routes>
    </HashRouter>
}


export default App