import { useEffect, useState } from "react";
import "./App.css";

const API = "http://localhost:8081/api";

function App() {
    const [programs, setPrograms] = useState([]);
    const [selected, setSelected] = useState(null);
    const [participants, setParticipants] = useState([]);
    const [notice, setNotice] = useState("");
    const [error, setError] = useState("");
    const [program, setProgram] = useState({ title: "", maxParticipants: "" });
    const [nomination, setNomination] = useState({ officerId: "", officerName: "", email: "", departmentName: "" });

    const loadPrograms = async () => {
        const response = await fetch(`${API}/training-programs`);
        if (!response.ok) throw new Error("Could not load training programmes");
        setPrograms(await response.json());
    };

    useEffect(() => { loadPrograms().catch((e) => setError(e.message)); }, []);

    const createProgram = async (event) => {
        event.preventDefault(); setError(""); setNotice("");
        const response = await fetch(`${API}/training-programs`, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify({ ...program, maxParticipants: Number(program.maxParticipants) }) });
        if (!response.ok) { setError(response.status === 409 ? "This programme already exists." : "Could not create programme."); return; }
        setNotice("Training programme created successfully."); setProgram({ title: "", maxParticipants: "" }); await loadPrograms();
    };

    const viewParticipants = async (title) => {
        const chosen = programs.find((item) => item.title === title) || selected;
        setSelected(chosen);
        const response = await fetch(`${API}/training-programs/${encodeURIComponent(title)}/participants`);
        if (response.ok) setParticipants(await response.json());
    };

    const addParticipant = async (event) => {
        event.preventDefault(); setError(""); setNotice("");
        if (!selected) { setError("Select a training programme first."); return; }
        const response = await fetch(`${API}/nominations`, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify({ ...nomination, trainingTitle: selected.title, maxParticipants: selected.maxParticipants }) });
        const text = await response.text();
        if (!response.ok) { setError(text || "Could not add participant."); return; }
        setNotice("Participant added successfully."); setNomination({ officerId: "", officerName: "", email: "", departmentName: "" }); await loadPrograms(); await viewParticipants(selected.title);
    };

    const cancelParticipant = async (id, title) => {
        setError(""); setNotice("");
        const response = await fetch(`${API}/nominations/${id}/cancel`, { method: "PATCH" });
        const text = await response.text();
        if (!response.ok) { setError(text || "Could not cancel participant."); return; }
        setNotice("Participant cancelled. The first waiting participant has been promoted when a confirmed seat was released.");
        await loadPrograms();
        await viewParticipants(title);
    };

    const updateNomination = (event) => setNomination({ ...nomination, [event.target.name]: event.target.value });
    const field = (name, label, type = "text") => <label className="field">{label}<input required name={name} type={type} value={nomination[name]} onChange={updateNomination} /></label>;

    return <main className="page">
        <header><p className="eyebrow">Training Management</p><h1>Build better programmes.<br /><em>Know every participant.</em></h1><p className="lead">Create a programme, set its seat limit, and manage the complete participant register from one place.</p></header>
        <section className="grid">
            <div className="panel"><div className="panel-head"><span>01</span><h2>Create training programme</h2></div><form onSubmit={createProgram}><label className="field">Programme title<input required value={program.title} onChange={(e) => setProgram({ ...program, title: e.target.value })} placeholder="e.g. Public Finance Essentials" /></label><label className="field">Maximum participants<input required min="1" type="number" value={program.maxParticipants} onChange={(e) => setProgram({ ...program, maxParticipants: e.target.value })} placeholder="40" /></label><button type="submit">Create programme <span>→</span></button></form></div>
            <div className="panel"><div className="panel-head"><span>02</span><h2>Add participant</h2></div><form onSubmit={addParticipant}>{field("officerId", "Officer ID")}{field("officerName", "Officer name")}{field("email", "Email", "email")}{field("departmentName", "Department")}<label className="field">Training programme<select required value={selected?.title || ""} onChange={(e) => { const value = e.target.value; setSelected(programs.find((item) => item.title === value) || null); }}><option value="">Select a programme</option>{programs.map((item) => <option key={item.id} value={item.title}>{item.title} · {item.participantCount}/{item.maxParticipants}</option>)}</select></label><button type="submit">Add participant <span>→</span></button></form></div>
        </section>
        <section className="panel register"><div className="panel-head"><span>03</span><h2>Programme register</h2></div>{programs.length === 0 ? <p className="empty">No programmes yet. Create your first programme above.</p> : <div className="program-list">{programs.map((item) => <button className="program" key={item.id} onClick={() => viewParticipants(item.title)}><span><strong>{item.title}</strong><small>{item.participantCount} participants registered</small></span><b>{item.participantCount} / {item.maxParticipants}</b></button>)}</div>}{selected && <div className="participants"><h3>{selected.title}</h3>{participants.length === 0 ? <p className="empty">No participants registered yet.</p> : <div className="table">{participants.map((person) => <div className="row" key={person.id}><strong>{person.officerName}</strong><span>{person.officerId}</span><span>{person.email}</span><span>{person.departmentName}</span><b className={person.status?.toLowerCase()}>{person.status}</b>{person.status === "CONFIRMED" && <button className="cancel" onClick={() => cancelParticipant(person.id, selected.title)}>Cancel</button>}</div>)}</div>}</div>}</section>
        {(notice || error) && <div className={`notice ${error ? "error" : "success"}`}>{error || notice}</div>}
    </main>;
}

export default App;
