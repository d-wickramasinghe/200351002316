// import { useEffect, useState } from "react";
// import "./App.css";

// const API = "http://localhost:8081/api";

// function App() {
//     const [programs, setPrograms] = useState([]);
//     const [selected, setSelected] = useState(null);
//     const [participants, setParticipants] = useState([]);
//     const [notice, setNotice] = useState("");
//     const [error, setError] = useState("");
//     const [program, setProgram] = useState({ title: "", maxParticipants: "" });
//     const [nomination, setNomination] = useState({ officerId: "", officerName: "", email: "", departmentName: "" });

//     const loadPrograms = async () => {
//         const response = await fetch(`${API}/training-programs`);
//         if (!response.ok) throw new Error("Could not load training programmes");
//         setPrograms(await response.json());
//     };

//     useEffect(() => { loadPrograms().catch((e) => setError(e.message)); }, []);

//     const createProgram = async (event) => {
//         event.preventDefault(); setError(""); setNotice("");
//         const response = await fetch(`${API}/training-programs`, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify({ ...program, maxParticipants: Number(program.maxParticipants) }) });
//         if (!response.ok) { setError(response.status === 409 ? "This programme already exists." : "Could not create programme."); return; }
//         setNotice("Training programme created successfully."); setProgram({ title: "", maxParticipants: "" }); await loadPrograms();
//     };

//     const viewParticipants = async (title) => {
//         const chosen = programs.find((item) => item.title === title) || selected;
//         setSelected(chosen);
//         const response = await fetch(`${API}/training-programs/${encodeURIComponent(title)}/participants`);
//         if (response.ok) setParticipants(await response.json());
//     };

//     const addParticipant = async (event) => {
//         event.preventDefault(); setError(""); setNotice("");
//         if (!selected) { setError("Select a training programme first."); return; }
//         const response = await fetch(`${API}/nominations`, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify({ ...nomination, trainingTitle: selected.title, maxParticipants: selected.maxParticipants }) });
//         const text = await response.text();
//         if (!response.ok) { setError(text || "Could not add participant."); return; }
//         setNotice("Participant added successfully."); setNomination({ officerId: "", officerName: "", email: "", departmentName: "" }); await loadPrograms(); await viewParticipants(selected.title);
//     };

//     const cancelParticipant = async (id, title) => {
//         setError(""); setNotice("");
//         const response = await fetch(`${API}/nominations/${id}/cancel`, { method: "PATCH" });
//         const text = await response.text();
//         if (!response.ok) { setError(text || "Could not cancel participant."); return; }
//         setNotice("Participant cancelled. The first waiting participant has been promoted when a confirmed seat was released.");
//         await loadPrograms();
//         await viewParticipants(title);
//     };

//     const updateNomination = (event) => setNomination({ ...nomination, [event.target.name]: event.target.value });
//     const field = (name, label, type = "text") => <label className="field">{label}<input required name={name} type={type} value={nomination[name]} onChange={updateNomination} /></label>;

//     return <main className="page">
//         <header><p className="eyebrow">Training Management</p><h1>Build better programmes.<br /><em>Know every participant.</em></h1><p className="lead">Create a programme, set its seat limit, and manage the complete participant register from one place.</p></header>
//         <section className="grid">
//             <div className="panel"><div className="panel-head"><span>01</span><h2>Create training programme</h2></div><form onSubmit={createProgram}><label className="field">Programme title<input required value={program.title} onChange={(e) => setProgram({ ...program, title: e.target.value })} placeholder="e.g. Public Finance Essentials" /></label><label className="field">Maximum participants<input required min="1" type="number" value={program.maxParticipants} onChange={(e) => setProgram({ ...program, maxParticipants: e.target.value })} placeholder="40" /></label><button type="submit">Create programme <span>→</span></button></form></div>
//             <div className="panel"><div className="panel-head"><span>02</span><h2>Add participant</h2></div><form onSubmit={addParticipant}>{field("officerId", "Officer ID")}{field("officerName", "Officer name")}{field("email", "Email", "email")}{field("departmentName", "Department")}<label className="field">Training programme<select required value={selected?.title || ""} onChange={(e) => { const value = e.target.value; setSelected(programs.find((item) => item.title === value) || null); }}><option value="">Select a programme</option>{programs.map((item) => <option key={item.id} value={item.title}>{item.title} · {item.participantCount}/{item.maxParticipants}</option>)}</select></label><button type="submit">Add participant <span>→</span></button></form></div>
//         </section>
//         <section className="panel register"><div className="panel-head"><span>03</span><h2>Programme register</h2></div>{programs.length === 0 ? <p className="empty">No programmes yet. Create your first programme above.</p> : <div className="program-list">{programs.map((item) => <button className="program" key={item.id} onClick={() => viewParticipants(item.title)}><span><strong>{item.title}</strong><small>{item.participantCount} participants registered</small></span><b>{item.participantCount} / {item.maxParticipants}</b></button>)}</div>}{selected && <div className="participants"><h3>{selected.title}</h3>{participants.length === 0 ? <p className="empty">No participants registered yet.</p> : <div className="table">{participants.map((person) => <div className="row" key={person.id}><strong>{person.officerName}</strong><span>{person.officerId}</span><span>{person.email}</span><span>{person.departmentName}</span><b className={person.status?.toLowerCase()}>{person.status}</b>{person.status === "CONFIRMED" && <button className="cancel" onClick={() => cancelParticipant(person.id, selected.title)}>Cancel</button>}</div>)}</div>}</div>}</section>
//         {(notice || error) && <div className={`notice ${error ? "error" : "success"}`}>{error || notice}</div>}
//     </main>;
// }

// export default App;


import { useEffect, useState } from "react";
import "./App.css";

const API = "http://localhost:8081/api";

const emptyNomination = {
    officerId: "",
    officerName: "",
    email: "",
    departmentName: "",
    grade: "",
    designation: "",
    yearsOfService: "",
};

const emptyRule = {
    ruleType: "DEPARTMENT",
    operator: "EQUALS",
    ruleValue: "",
};

function App() {
    const [programs, setPrograms] = useState([]);
    const [selected, setSelected] = useState(null);
    const [participants, setParticipants] = useState([]);

    const [notice, setNotice] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const [program, setProgram] = useState({
        title: "",
        maxParticipants: "",
    });

    const [nomination, setNomination] = useState(emptyNomination);

    const [rule, setRule] = useState(emptyRule);
    const [rules, setRules] = useState([]);

    const [showRuleForm, setShowRuleForm] = useState(false);

    const clearMessages = () => {
        setError("");
        setNotice("");
    };

    const getErrorMessage = async (response, fallback) => {
        const text = await response.text();

        if (!text) {
            return fallback;
        }

        try {
            const data = JSON.parse(text);

            if (typeof data === "string") {
                return data;
            }

            return (
                data.message ||
                data.error ||
                data.detail ||
                fallback
            );
        } catch {
            return text;
        }
    };

    // ==========================================================
    // LOAD PROGRAMMES
    // ==========================================================

    const loadPrograms = async () => {
        const response = await fetch(`${API}/training-programs`);

        if (!response.ok) {
            throw new Error("Could not load training programmes.");
        }

        const data = await response.json();
        setPrograms(data);
    };

    useEffect(() => {
        loadPrograms().catch((e) => setError(e.message));
    }, []);

    // ==========================================================
    // LOAD PARTICIPANTS
    // ==========================================================

    const viewParticipants = async (title) => {
        clearMessages();

        const chosen =
            programs.find((item) => item.title === title) ||
            selected;

        setSelected(chosen || null);

        try {
            const response = await fetch(
                `${API}/training-programs/${encodeURIComponent(title)}/participants`
            );

            if (!response.ok) {
                throw new Error("Could not load participants.");
            }

            const data = await response.json();

            setParticipants(data);

            await loadRules(title);
        } catch (e) {
            setError(e.message);
        }
    };

    // ==========================================================
    // CREATE PROGRAMME
    // ==========================================================

    const createProgram = async (event) => {
        event.preventDefault();
        clearMessages();
        setLoading(true);

        try {
            const response = await fetch(`${API}/training-programs`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    title: program.title.trim(),
                    maxParticipants: Number(
                        program.maxParticipants
                    ),
                }),
            });

            if (!response.ok) {
                const message = await getErrorMessage(
                    response,
                    response.status === 409
                        ? "This programme already exists."
                        : "Could not create programme."
                );

                throw new Error(message);
            }

            setNotice(
                "Training programme created successfully."
            );

            setProgram({
                title: "",
                maxParticipants: "",
            });

            await loadPrograms();
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    // ==========================================================
    // NOMINATION INPUT
    // ==========================================================

    const updateNomination = (event) => {
        const { name, value } = event.target;

        setNomination((current) => ({
            ...current,
            [name]: value,
        }));
    };

    // ==========================================================
    // ADD NOMINATION
    // ==========================================================

    const addParticipant = async (event) => {
        event.preventDefault();
        clearMessages();

        if (!selected) {
            setError("Select a training programme first.");
            return;
        }

        setLoading(true);

        try {
            const response = await fetch(`${API}/nominations`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    officerId: nomination.officerId.trim(),
                    officerName: nomination.officerName.trim(),
                    email: nomination.email.trim(),
                    trainingTitle: selected.title,
                    maxParticipants: selected.maxParticipants,
                    departmentName:
                        nomination.departmentName.trim(),
                    grade: nomination.grade.trim(),
                    designation:
                        nomination.designation.trim(),
                    yearsOfService:
                        Number(nomination.yearsOfService),
                }),
            });

            if (!response.ok) {
                const message = await getErrorMessage(
                    response,
                    "Could not add participant."
                );

                throw new Error(message);
            }

            const data = await response.json();

            if (data.status === "WAITING") {
                setNotice(
                    "Programme is full. Participant has been added to the waiting list."
                );
            } else {
                setNotice(
                    "Participant added successfully and confirmed."
                );
            }

            setNomination(emptyNomination);

            await loadPrograms();
            await viewParticipants(selected.title);
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    // ==========================================================
    // CANCEL NOMINATION
    // ==========================================================

    const cancelParticipant = async (id, title) => {
        clearMessages();

        const confirmed = window.confirm(
            "Are you sure you want to cancel this nomination?"
        );

        if (!confirmed) {
            return;
        }

        setLoading(true);

        try {
            const response = await fetch(
                `${API}/nominations/${id}/cancel`,
                {
                    method: "PATCH",
                }
            );

            if (!response.ok) {
                const message = await getErrorMessage(
                    response,
                    "Could not cancel participant."
                );

                throw new Error(message);
            }

            setNotice(
                "Nomination cancelled. The first waiting participant has been promoted if a confirmed seat was released."
            );

            await loadPrograms();
            await viewParticipants(title);
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    // ==========================================================
    // MARK PARTICIPATED
    // ==========================================================

    const markParticipated = async (id, title) => {
        clearMessages();
        setLoading(true);

        try {
            const response = await fetch(
                `${API}/nominations/${id}/participated`,
                {
                    method: "PATCH",
                }
            );

            if (!response.ok) {
                const message = await getErrorMessage(
                    response,
                    "Could not mark participation."
                );

                throw new Error(message);
            }

            setNotice(
                "Participation recorded successfully."
            );

            await loadPrograms();
            await viewParticipants(title);
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    // ==========================================================
    // LOAD ELIGIBILITY RULES
    // ==========================================================

    const loadRules = async (title) => {
        try {
            const response = await fetch(
                `${API}/eligibility-rules/${encodeURIComponent(title)}`
            );

            if (!response.ok) {
                throw new Error(
                    "Could not load eligibility rules."
                );
            }

            const data = await response.json();

            setRules(data);
        } catch (e) {
            setRules([]);
            setError(e.message);
        }
    };

    // ==========================================================
    // CREATE ELIGIBILITY RULE
    // ==========================================================

    const createRule = async (event) => {
        event.preventDefault();
        clearMessages();

        if (!selected) {
            setError("Select a training programme first.");
            return;
        }

        if (!rule.ruleValue.trim()) {
            setError("Enter an eligibility rule value.");
            return;
        }

        setLoading(true);

        try {
            const response = await fetch(
                `${API}/eligibility-rules`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        trainingTitle: selected.title,
                        ruleType: rule.ruleType,
                        operator: rule.operator,
                        ruleValue: rule.ruleValue.trim(),
                    }),
                }
            );

            if (!response.ok) {
                const message = await getErrorMessage(
                    response,
                    "Could not create eligibility rule."
                );

                throw new Error(message);
            }

            setNotice(
                "Eligibility rule added successfully."
            );

            setRule(emptyRule);

            await loadRules(selected.title);
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    // ==========================================================
    // RULE INPUT
    // ==========================================================

    const updateRule = (event) => {
        const { name, value } = event.target;

        setRule((current) => ({
            ...current,
            [name]: value,
        }));
    };

    // ==========================================================
    // SELECT PROGRAMME
    // ==========================================================

    const selectProgram = async (item) => {
        setSelected(item);
        setNomination(emptyNomination);
        await viewParticipants(item.title);
    };

    // ==========================================================
    // HELPERS
    // ==========================================================

    const getStatusClass = (status) => {
        if (!status) return "";

        return status.toLowerCase();
    };

    const formatDate = (value) => {
        if (!value) return "—";

        return new Date(value).toLocaleString();
    };

    const confirmedCount = participants.filter(
        (item) => item.status === "CONFIRMED"
    ).length;

    const waitingCount = participants.filter(
        (item) => item.status === "WAITING"
    ).length;

    // ==========================================================
    // UI
    // ==========================================================

    return (
        <main className="page">

            {/* ==================================================
                HEADER
            ================================================== */}

            <header className="hero">
                <div>
                    <p className="eyebrow">
                        Training Management · Task 03
                    </p>

                    <h1>
                        Smarter training.
                        <br />
                        <em>Fairer selection.</em>
                    </h1>

                    <p className="lead">
                        Create programmes, define eligibility,
                        manage capacity, track participation,
                        and automatically manage the waiting list.
                    </p>
                </div>

                <div className="task-badge">
                    <span>ACTIVE</span>
                    <strong>Eligibility + Participation</strong>
                </div>
            </header>

            {/* ==================================================
                TOP CARDS
            ================================================== */}

            <section className="grid">

                {/* CREATE PROGRAMME */}

                <div className="panel">
                    <div className="panel-head">
                        <span>01</span>

                        <div>
                            <h2>
                                Create training programme
                            </h2>

                            <p>
                                Set the programme name and
                                maximum capacity.
                            </p>
                        </div>
                    </div>

                    <form onSubmit={createProgram}>

                        <label className="field">
                            Programme title

                            <input
                                required
                                value={program.title}
                                onChange={(e) =>
                                    setProgram({
                                        ...program,
                                        title: e.target.value,
                                    })
                                }
                                placeholder="e.g. Public Finance Essentials"
                            />
                        </label>

                        <label className="field">
                            Maximum participants

                            <input
                                required
                                min="1"
                                type="number"
                                value={
                                    program.maxParticipants
                                }
                                onChange={(e) =>
                                    setProgram({
                                        ...program,
                                        maxParticipants:
                                            e.target.value,
                                    })
                                }
                                placeholder="40"
                            />
                        </label>

                        <button
                            type="submit"
                            disabled={loading}
                        >
                            {loading
                                ? "Creating..."
                                : "Create programme"}

                            <span>→</span>
                        </button>
                    </form>
                </div>

                {/* ADD PARTICIPANT */}

                <div className="panel">
                    <div className="panel-head">
                        <span>02</span>

                        <div>
                            <h2>
                                Nominate an officer
                            </h2>

                            <p>
                                Eligibility is checked by the
                                backend before confirmation.
                            </p>
                        </div>
                    </div>

                    <form onSubmit={addParticipant}>

                        <div className="two-fields">

                            <label className="field">
                                Officer ID

                                <input
                                    required
                                    name="officerId"
                                    value={
                                        nomination.officerId
                                    }
                                    onChange={
                                        updateNomination
                                    }
                                    placeholder="OFF001"
                                />
                            </label>

                            <label className="field">
                                Officer name

                                <input
                                    required
                                    name="officerName"
                                    value={
                                        nomination.officerName
                                    }
                                    onChange={
                                        updateNomination
                                    }
                                    placeholder="Kamal Perera"
                                />
                            </label>

                        </div>

                        <label className="field">
                            Email

                            <input
                                required
                                type="email"
                                name="email"
                                value={nomination.email}
                                onChange={
                                    updateNomination
                                }
                                placeholder="officer@example.com"
                            />
                        </label>

                        <div className="two-fields">

                            <label className="field">
                                Department

                                <input
                                    required
                                    name="departmentName"
                                    value={
                                        nomination.departmentName
                                    }
                                    onChange={
                                        updateNomination
                                    }
                                    placeholder="Finance"
                                />
                            </label>

                            <label className="field">
                                Grade

                                <input
                                    required
                                    name="grade"
                                    value={nomination.grade}
                                    onChange={
                                        updateNomination
                                    }
                                    placeholder="Grade III"
                                />
                            </label>

                        </div>

                        <div className="two-fields">

                            <label className="field">
                                Designation

                                <input
                                    required
                                    name="designation"
                                    value={
                                        nomination.designation
                                    }
                                    onChange={
                                        updateNomination
                                    }
                                    placeholder="Accountant"
                                />
                            </label>

                            <label className="field">
                                Years of service

                                <input
                                    required
                                    min="0"
                                    type="number"
                                    name="yearsOfService"
                                    value={
                                        nomination.yearsOfService
                                    }
                                    onChange={
                                        updateNomination
                                    }
                                    placeholder="7"
                                />
                            </label>

                        </div>

                        <label className="field">
                            Training programme

                            <select
                                required
                                value={
                                    selected?.title || ""
                                }
                                onChange={(e) => {
                                    const item =
                                        programs.find(
                                            (programItem) =>
                                                programItem.title ===
                                                e.target.value
                                        );

                                    if (item) {
                                        selectProgram(item);
                                    } else {
                                        setSelected(null);
                                        setParticipants([]);
                                        setRules([]);
                                    }
                                }}
                            >
                                <option value="">
                                    Select a programme
                                </option>

                                {programs.map((item) => (
                                    <option
                                        key={item.id}
                                        value={item.title}
                                    >
                                        {item.title} ·{" "}
                                        {item.participantCount}/
                                        {item.maxParticipants}
                                    </option>
                                ))}
                            </select>
                        </label>

                        {selected && (
                            <div className="capacity-preview">
                                <span>
                                    Available seats
                                </span>

                                <strong>
                                    {Math.max(
                                        selected.maxParticipants -
                                            participants.filter(
                                                (p) =>
                                                    p.status ===
                                                    "CONFIRMED"
                                            ).length,
                                        0
                                    )}
                                </strong>
                            </div>
                        )}

                        <button
                            type="submit"
                            disabled={loading || !selected}
                        >
                            {loading
                                ? "Processing..."
                                : "Submit nomination"}

                            <span>→</span>
                        </button>

                    </form>
                </div>
            </section>

            {/* ==================================================
                PROGRAMME REGISTER
            ================================================== */}

            <section className="panel register">

                <div className="panel-head">
                    <span>03</span>

                    <div>
                        <h2>Programme register</h2>

                        <p>
                            Select a programme to manage its
                            rules and participants.
                        </p>
                    </div>
                </div>

                {programs.length === 0 ? (
                    <p className="empty">
                        No programmes yet. Create your first
                        programme above.
                    </p>
                ) : (
                    <div className="program-list">

                        {programs.map((item) => (
                            <button
                                className={`program ${
                                    selected?.id === item.id
                                        ? "active"
                                        : ""
                                }`}
                                key={item.id}
                                onClick={() =>
                                    selectProgram(item)
                                }
                            >
                                <span>
                                    <strong>
                                        {item.title}
                                    </strong>

                                    <small>
                                        {item.participantCount}{" "}
                                        registrations
                                    </small>
                                </span>

                                <b>
                                    {item.participantCount} /{" "}
                                    {item.maxParticipants}
                                </b>
                            </button>
                        ))}

                    </div>
                )}

                {/* ==================================================
                    SELECTED PROGRAMME
                ================================================== */}

                {selected && (
                    <div className="programme-details">

                        <div className="details-header">

                            <div>
                                <p className="mini-label">
                                    Selected programme
                                </p>

                                <h3>
                                    {selected.title}
                                </h3>
                            </div>

                            <div className="stats">

                                <div>
                                    <span>
                                        Confirmed
                                    </span>

                                    <strong>
                                        {confirmedCount}
                                    </strong>
                                </div>

                                <div>
                                    <span>
                                        Waiting
                                    </span>

                                    <strong>
                                        {waitingCount}
                                    </strong>
                                </div>

                                <div>
                                    <span>
                                        Capacity
                                    </span>

                                    <strong>
                                        {selected.maxParticipants}
                                    </strong>
                                </div>

                            </div>
                        </div>

                        {/* ==================================================
                            ELIGIBILITY
                        ================================================== */}

                        <div className="rules-section">

                            <div className="section-title">

                                <div>
                                    <span className="number">
                                        04
                                    </span>

                                    <div>
                                        <h3>
                                            Eligibility rules
                                        </h3>

                                        <p>
                                            Every configured rule
                                            must be satisfied.
                                        </p>
                                    </div>
                                </div>

                                <button
                                    className="secondary-button"
                                    onClick={() =>
                                        setShowRuleForm(
                                            !showRuleForm
                                        )
                                    }
                                >
                                    {showRuleForm
                                        ? "Close"
                                        : "+ Add rule"}
                                </button>

                            </div>

                            {rules.length === 0 ? (
                                <div className="no-rules">
                                    <span>✓</span>

                                    <div>
                                        <strong>
                                            No eligibility
                                            restrictions
                                        </strong>

                                        <p>
                                            All officers can be
                                            nominated unless a
                                            programme rule is added.
                                        </p>
                                    </div>
                                </div>
                            ) : (
                                <div className="rules-list">

                                    {rules.map((item) => (
                                        <div
                                            className="rule-card"
                                            key={item.id}
                                        >
                                            <span className="rule-type">
                                                {item.ruleType}
                                            </span>

                                            <strong>
                                                {item.operator}
                                            </strong>

                                            <span>
                                                {item.ruleValue}
                                            </span>
                                        </div>
                                    ))}

                                </div>
                            )}

                            {showRuleForm && (
                                <form
                                    className="rule-form"
                                    onSubmit={createRule}
                                >

                                    <label className="field">
                                        Rule type

                                        <select
                                            name="ruleType"
                                            value={
                                                rule.ruleType
                                            }
                                            onChange={
                                                updateRule
                                            }
                                        >
                                            <option value="DEPARTMENT">
                                                Department
                                            </option>

                                            <option value="GRADE">
                                                Grade
                                            </option>

                                            <option value="DESIGNATION">
                                                Designation
                                            </option>

                                            <option value="YEARS_OF_SERVICE">
                                                Years of service
                                            </option>
                                        </select>
                                    </label>

                                    <label className="field">
                                        Operator

                                        <select
                                            name="operator"
                                            value={
                                                rule.operator
                                            }
                                            onChange={
                                                updateRule
                                            }
                                        >
                                            <option value="EQUALS">
                                                Equals
                                            </option>
                                        </select>
                                    </label>

                                    <label className="field">
                                        Required value

                                        <input
                                            required
                                            name="ruleValue"
                                            value={
                                                rule.ruleValue
                                            }
                                            onChange={
                                                updateRule
                                            }
                                            placeholder={
                                                rule.ruleType ===
                                                "YEARS_OF_SERVICE"
                                                    ? "5"
                                                    : "Finance"
                                            }
                                        />
                                    </label>

                                    <button
                                        type="submit"
                                        disabled={loading}
                                    >
                                        Add eligibility rule
                                        <span>→</span>
                                    </button>

                                </form>
                            )}

                        </div>

                        {/* ==================================================
                            PARTICIPANTS
                        ================================================== */}

                        <div className="participants">

                            <div className="section-title">

                                <div>
                                    <span className="number">
                                        05
                                    </span>

                                    <div>
                                        <h3>
                                            Participant register
                                        </h3>

                                        <p>
                                            Confirmed, waiting and
                                            participation history.
                                        </p>
                                    </div>
                                </div>

                            </div>

                            {participants.length === 0 ? (
                                <p className="empty">
                                    No participants registered
                                    yet.
                                </p>
                            ) : (
                                <div className="table">

                                    <div className="table-head">
                                        <span>Officer</span>
                                        <span>Department</span>
                                        <span>Experience</span>
                                        <span>Status</span>
                                        <span>Participation</span>
                                        <span>Action</span>
                                    </div>

                                    {participants.map(
                                        (person) => (
                                            <div
                                                className="row"
                                                key={person.id}
                                            >

                                                <div className="officer">
                                                    <strong>
                                                        {
                                                            person.officerName
                                                        }
                                                    </strong>

                                                    <small>
                                                        {
                                                            person.officerId
                                                        }
                                                    </small>

                                                    <small>
                                                        {
                                                            person.email
                                                        }
                                                    </small>
                                                </div>

                                                <div>
                                                    <span>
                                                        {
                                                            person.departmentName
                                                        }
                                                    </span>

                                                    <small className="muted-line">
                                                        {
                                                            person.grade
                                                        }
                                                    </small>
                                                </div>

                                                <div>
                                                    <strong>
                                                        {
                                                            person.yearsOfService
                                                        }
                                                    </strong>

                                                    <small className="muted-line">
                                                        years
                                                    </small>
                                                </div>

                                                <div>
                                                    <b
                                                        className={`status ${getStatusClass(
                                                            person.status
                                                        )}`}
                                                    >
                                                        {
                                                            person.status
                                                        }
                                                    </b>
                                                </div>

                                                <div className="participation">

                                                    {person.participatedAt ? (
                                                        <>
                                                            <b className="participated">
                                                                Recorded
                                                            </b>

                                                            <small>
                                                                {formatDate(
                                                                    person.participatedAt
                                                                )}
                                                            </small>
                                                        </>
                                                    ) : (
                                                        <span className="not-participated">
                                                            Not recorded
                                                        </span>
                                                    )}

                                                </div>

                                                <div className="actions">

                                                    {person.status ===
                                                        "CONFIRMED" &&
                                                        !person.participatedAt && (
                                                            <button
                                                                className="participate"
                                                                onClick={() =>
                                                                    markParticipated(
                                                                        person.id,
                                                                        selected.title
                                                                    )
                                                                }
                                                            >
                                                                Mark attended
                                                            </button>
                                                        )}

                                                    {person.status ===
                                                        "CONFIRMED" && (
                                                            <button
                                                                className="cancel"
                                                                onClick={() =>
                                                                    cancelParticipant(
                                                                        person.id,
                                                                        selected.title
                                                                    )
                                                                }
                                                            >
                                                                Cancel
                                                            </button>
                                                        )}

                                                </div>

                                            </div>
                                        )
                                    )}

                                </div>
                            )}

                        </div>

                    </div>
                )}

            </section>

            {/* ==================================================
                NOTIFICATIONS
            ================================================== */}

            {(notice || error) && (
                <div
                    className={`notice ${
                        error ? "error" : "success"
                    }`}
                >
                    <span>
                        {error ? "!" : "✓"}
                    </span>

                    <div>
                        <strong>
                            {error
                                ? "Action failed"
                                : "Success"}
                        </strong>

                        <p>
                            {error || notice}
                        </p>
                    </div>

                    <button
                        className="close-notice"
                        onClick={clearMessages}
                    >
                        ×
                    </button>
                </div>
            )}

        </main>
    );
}

export default App;