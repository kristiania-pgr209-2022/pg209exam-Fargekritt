import {useEffect, useState} from 'react'
import './App.css'
import {HashRouter, Link, Route, Routes, useNavigate} from "react-router-dom";
import Select from "react-select";


function FrontPage() {
    const navigate = useNavigate();
    return <div>
        <h1>CHAT ROOM OF DOOM!</h1>
        <button onClick={() => navigate("/users")}>List users</button>
        <button onClick={() => navigate("/users/add")}>Add a new user</button>
    </div>;
}

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
                    <div><label>Username</label>
                        <br/>
                        {u.username}</div>
                    <div><label>FirstName</label>
                        <br/>
                        {u.firstName}</div>
                    <div><label>LastName</label>
                        <br/>
                        {u.lastName}</div>
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
        <Link to={"/"}>Back to home</Link>
        <div style={{border: '1px solid black'}}><h3>UserInfo</h3>
            <div><label>Email: </label>
                {user.email}</div>
            <div><label>Username: </label>
                {user.username}</div>
            <div><label>FirstName: </label>
                {user.firstName}</div>
            <div><label>LastName: </label>
                {user.lastName}</div>
            <div><label>Gender: </label>
                {user.gender}</div>
            <div><label>Date of Birth:</label>
                <br/>
                {user.dateOfBirth.substring(0, 4)}</div>
            <div><Link to={"/users/edit"}>Edit user info</Link></div>
        </div>

        <h1>Threads your a member of</h1>
        <div>
            {thread.map((t) => (
                <button value={t} onClick={() => handleClick(t)} style={{border: '1px solid black'}}>

                    <div><label>ThreadID: </label>{t.id}</div>
                    <div><label>Creator: </label>{t.creator.username}</div>
                    <div><label>Title: </label>{t.title}</div>

                </button>
            ))}
        </div>
        <div><Link to={"/threads/add"}>Start a new chat</Link></div>
    </div>
}

function ListMessages({thread, members, setMembers}) {

    const [loading, setLoading] = useState(true);
    const [loading2, setLoading2] = useState(true);
    const [message, setMessage] = useState([]);

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

    function date(i) {
        i = i.sentDate.substring(0, 10).replaceAll("-", ".")
        const dateArray = i.split(".")
        console.log(dateArray)
        return dateArray[2] + "." + dateArray[1] + "." + dateArray[0]
    }

    return <div>
        <Link to={"/"}>Back to home</Link>
        <div style={{border: '4px solid black'}}>
            <h3>ThreadMembers:</h3>
            {members.map((i) => (
                <p>{i.username}</p>
            ))}
        </div>
        <div style={{border: '2px solid black', marginTop: '7px'}}><label>Thread title: </label>
            {thread.title}
        </div>
        <p>&#8595;</p>
        <div style={{border: '2px solid black'}}>
            <label>Messages:</label>
        </div>
        <div>
            {message.map((i) => (
                <div>
                    <p>&#8595;</p>
                    <div style={{border: '2px solid black'}}>
                        <div><label>Title: </label>{i.title}</div>
                        <div><label>From: </label>{i.user.username}</div>
                        <div><label>Date sent: </label>{date(i)}</div>
                        <div><label>Time sent: </label>{i.sentDate.substring(11).replaceAll("-", ":")}</div>
                        <div><label>Message</label>
                            <br/>
                            {i.body}
                        </div>
                    </div>
                </div>
            ))}
        </div>
        <div><Link to={"/messages/add"}>Add message</Link></div>
        <div><Link to={"/threads/members/add"}>Add a member to the chat</Link></div>
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
    const [email, setEmail] = useState("");
    const [birthDate, setBirthDate] = useState("");

    async function handleSubmit(e) {

        e.preventDefault();
        const dateOfBirth = birthDate + "-01-01:01-01-01"
        await fetch("/api/users", {
            method: "post",
            body: JSON.stringify({firstName, lastName, gender, username, email, dateOfBirth}),
            headers: {
                "Content-Type": "application/json"
            }
        });

        navigate("/users")
    }

    const birthDateOptionsYear = () => {
        const dateArray = [];

        const yearStart = 1910;
        const yearEnd = new Date().getFullYear();


        for (let i = yearEnd; i >= yearStart; i--) {
            dateArray.push(<option value={i}>{i}</option>)
        }
        return dateArray;
    }

    return (
        <div className="App">
            <h1>Add User</h1>
            <Link to={"/"}>Back to home</Link>
            <form onSubmit={handleSubmit}>
                <div><label>First Name: <input type="text" onChange={e => setFirstName(e.target.value)}/></label></div>
                <div><label>Last Name: <input type="text" onChange={e => setLastName(e.target.value)}/></label></div>
                <div><label>Username: <input type="text" onChange={e => setUsername(e.target.value)}/></label></div>
                <div><label>Gender: <input type="text" onChange={e => setGender(e.target.value)}></input></label></div>
                <div><label>Email: <input type="text" onChange={e => setEmail(e.target.value)}></input></label></div>
                <div><label>Birth date:
                    <select name="birth-date" onChange={(e) => setBirthDate(e.target.value)}>
                        <option value="0">Year</option>
                        {birthDateOptionsYear()}
                    </select>
                </label>
                </div>

                <button>Submit</button>
            </form>
        </div>
    )
}

function AddThread({creator}) {

    const navigate = useNavigate()
    const [threadTitle, setThreadTitle] = useState();
    const [loading, setLoading] = useState();
    const [message, setMessage] = useState();
    const [users, setUsers] = useState([]);
    const [members, setMembers] = useState([]);
    const [messageTitle, setMessageTitle] = useState();

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

        e.preventDefault();

        const request = {
            method: "post",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({creator, members, threadTitle, message, messageTitle})
        }
        await fetch("/api/thread/", request)

        navigate("/users/")
    }


    const filteredOptions = users.filter(
        ({id}) => creator.id !== id
    );


    return (
        <div className="App">
            <h1>Start Chat</h1>
            <Link to={"/"}>Back to home</Link>
            <form onSubmit={handleSubmit}>
                <div><label>Thread title: <br/><input type="text"
                                                      onChange={e => setThreadTitle(e.target.value)}/></label></div>
                <div><label>Message title: <br/><input type="text" onChange={e => setMessageTitle(e.target.value)}/></label>
                </div>
                <div><label>Message: <br/><textarea onChange={e => setMessage(e.target.value)}></textarea></label>
                    <div><label>Select the members u want in the chat:</label>

                        <Select name="users"
                                id={"user"}
                                options={filteredOptions}
                                onChange={setMembers}
                                getOptionLabel={(option) => option.username}
                                getOptionValue={(option) => option.id}
                                isMulti
                        /></div>
                </div>
                <button>Submit</button>
            </form>
        </div>
    )
}

function AddMessage({user, thread}) {
    const navigate = useNavigate()
    const [body, setBody] = useState();
    const [title, setTitle] = useState("");

    async function handleSubmit(e) {

        e.preventDefault();

        const request = {
            method: "post",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({body, user, thread, title})
        }
        await fetch("/api/messages", request)

        navigate("/messages")
    }

    return (
        <div className="App">
            <h1>Create message</h1>
            <Link to={"/"}>Back to home</Link>
            <form onSubmit={handleSubmit}>
                <div><label>Title: <input onChange={e => setTitle(e.target.value)}></input></label>
                </div>
                <div><label>Message: <textarea onChange={e => setBody(e.target.value)}></textarea></label>
                </div>
                <button>Submit</button>
            </form>
        </div>)
}

function AddMember({thread, members}) {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [users, setUsers] = useState([]);
    const [user, setUser] = useState();
    const knownMembers = [];
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
    for (let i = 0; i < members.length; i++) {
        knownMembers.push(members[i].id)
    }

    const filteredOptions = users.filter(
        ({id}) => !knownMembers.includes(id)
    );

    return (
        <div className="App">
            <h1>Add member to thread</h1>
            <Link to={"/"}>Back to home</Link>
            <form onSubmit={handleSubmit}>
                <label>Choose a member:</label>

                <Select name="users"
                        id={"user"}
                        options={filteredOptions}
                        value={user}
                        onChange={setUser}
                        getOptionLabel={(option) => option.username}
                        getOptionValue={(option) => option.id}
                />
                <button>Add Member</button>
            </form>
        </div>
    )
}


//=============================================ADD END=============================================================


function EditUser({user}) {
    const navigate = useNavigate()
    const [firstName, setFirstName] = useState(user.firstName);
    const [lastName, setLastName] = useState(user.lastName);
    const [username, setUsername] = useState(user.username);
    const [gender, setGender] = useState(user.gender);
    const [email, setEmail] = useState(user.email);

    async function handleSubmit(e) {

        e.preventDefault();
        await fetch("/api/users/" + user.id, {
            method: "post",
            body: JSON.stringify({firstName, lastName, gender, username, email}),
            headers: {
                "Content-Type": "application/json"
            }
        });

        navigate("/users")
    }

    return (
        <div className="App">
            <h1>Edit User</h1>
            <Link to={"/"}>Back to home</Link>
            <form onSubmit={handleSubmit}>
                <div><label>First Name: <input defaultValue={user.firstName} type="text"
                                               onChange={e => setFirstName(e.target.value)}/></label></div>
                <div><label>Last Name: <input defaultValue={user.lastName} type="text"
                                              onChange={e => setLastName(e.target.value)}/></label></div>
                <div><label>Username: <input defaultValue={user.username} type="text"
                                             onChange={e => setUsername(e.target.value)}/></label></div>
                <div><label>Gender: <input defaultValue={user.gender} type="text"
                                           onChange={e => setGender(e.target.value)}></input></label></div>
                <div><label>Email: <input defaultValue={user.email} type="text"
                                          onChange={e => setEmail(e.target.value)}></input></label></div>
                <button>Submit</button>
            </form>
        </div>
    )
}

function App() {
    const [user, setUser] = useState();
    const [thread, setThread] = useState();
    const [members, setMembers] = useState();

    return <HashRouter>
        <Routes>
            <Route path={"/"} element={<FrontPage/>}></Route>
            <Route path={"/users"} element={<ListUser setSelectedUser={setUser}/>}></Route>
            <Route path={"/users/add"} element={<AddUser/>}></Route>
            <Route path={"/users/edit"} element={<EditUser user={user}/>}></Route>
            <Route path={"/threads"} element={<ListThreads user={user} setSelectedThread={setThread}/>}></Route>
            <Route path={"/threads/add"} element={<AddThread creator={user}/>}></Route>
            <Route path={"/messages"}
                   element={<ListMessages thread={thread} setMembers={setMembers} members={members}/>}></Route>
            <Route path={"/messages/add"} element={<AddMessage user={user} thread={thread}/>}></Route>
            <Route path={"threads/members/add"} element={<AddMember thread={thread} members={members}/>}></Route>

        </Routes>
    </HashRouter>
}


export default App