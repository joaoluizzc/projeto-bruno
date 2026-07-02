const modules = {
    areas: {
        endpoint: "/api/areas",
        title: "Areas do conhecimento",
        item: "area",
        fields: [
            { name: "nome", label: "Nome da area", placeholder: "Ex.: Ciencias Exatas" }
        ]
    },
    professores: {
        endpoint: "/api/professores",
        title: "Professores",
        item: "professor",
        fields: [
            { name: "nome", label: "Nome do professor", placeholder: "Ex.: Ana Martins" },
            { name: "email", label: "Email", placeholder: "ana@uniguacu.edu.br", type: "email" }
        ],
        columns: [
            { label: "Email", path: "email" }
        ]
    },
    cursos: {
        endpoint: "/api/cursos",
        title: "Cursos",
        item: "curso",
        fields: [
            { name: "nome", label: "Nome do curso", placeholder: "Ex.: Engenharia de Software" },
            { name: "areaConhecimentoId", label: "Area do conhecimento", source: "areas", editPath: "areaConhecimento.id" },
            { name: "coordenadorId", label: "Coordenador", source: "professores", editPath: "coordenador.id" }
        ],
        columns: [
            { label: "Area", path: "areaConhecimento" },
            { label: "Coordenador", path: "coordenador" }
        ]
    },
    disciplinas: {
        endpoint: "/api/disciplinas",
        title: "Disciplinas",
        item: "disciplina",
        fields: [
            { name: "nome", label: "Nome da disciplina", placeholder: "Ex.: Banco de Dados" },
            { name: "cursoId", label: "Curso", source: "cursos", editPath: "curso.id" }
        ],
        columns: [
            { label: "Curso", path: "curso" }
        ]
    }
};

const state = {
    view: "areas",
    editingId: null,
    search: "",
    data: Object.fromEntries(Object.keys(modules).map((key) => [key, []]))
};

const $ = (selector) => document.querySelector(selector);
const elements = {
    navButtons: document.querySelectorAll("[data-view]"),
    pageTitle: $("#pageTitle"),
    refreshButton: $("#refreshButton"),
    entityForm: $("#entityForm"),
    formFields: $("#formFields"),
    formKicker: $("#formKicker"),
    formTitle: $("#formTitle"),
    submitLabel: $("#submitLabel"),
    cancelEditButton: $("#cancelEditButton"),
    feedback: $("#feedback"),
    searchInput: $("#searchInput"),
    resultCount: $("#resultCount"),
    tableHead: $("#tableHead"),
    tableBody: $("#tableBody"),
    statusText: $("#statusText"),
    totalGeral: $("#totalGeral"),
    metricAreas: $("#metricAreas"),
    metricProfessores: $("#metricProfessores"),
    metricCursos: $("#metricCursos"),
    metricDisciplinas: $("#metricDisciplinas")
};

async function requestJson(url, options = {}) {
    const response = await fetch(url, {
        headers: { "Content-Type": "application/json" },
        ...options
    });

    if (!response.ok) {
        const detail = await response.json().catch(() => ({}));
        const message = detail.message || detail.error || Object.values(detail).join(". ");
        throw new Error(message || "Nao foi possivel completar a acao.");
    }

    return response.status === 204 ? null : response.json();
}

async function loadAll() {
    setStatus("Carregando dados...");

    try {
        const loaded = await Promise.all(Object.entries(modules).map(async ([key, module]) => {
            return [key, sortByName(await requestJson(module.endpoint))];
        }));

        loaded.forEach(([key, records]) => {
            state.data[key] = records;
        });

        setStatus("Dados atualizados");
        setFeedback("", "");
    } catch (error) {
        setStatus("Backend indisponivel");
        setFeedback(error.message, "error");
    }

    render();
}

function render() {
    renderShell();
    renderForm();
    renderTable();
    refreshIcons();
}

function renderShell() {
    elements.pageTitle.textContent = currentModule().title;

    elements.navButtons.forEach((button) => {
        button.classList.toggle("is-active", button.dataset.view === state.view);
    });

    elements.metricAreas.textContent = state.data.areas.length;
    elements.metricProfessores.textContent = state.data.professores.length;
    elements.metricCursos.textContent = state.data.cursos.length;
    elements.metricDisciplinas.textContent = state.data.disciplinas.length;
    elements.totalGeral.textContent = Object.values(state.data).reduce((total, list) => total + list.length, 0);
}

function renderForm() {
    const { item, fields } = currentModule();
    const editing = Boolean(state.editingId);

    elements.formKicker.textContent = editing ? "Editando registro" : "Novo cadastro";
    elements.formTitle.textContent = `${editing ? "Editar" : "Cadastrar"} ${item}`;
    elements.submitLabel.textContent = editing ? "Atualizar cadastro" : "Salvar cadastro";
    elements.cancelEditButton.classList.toggle("is-hidden", !editing);
    elements.formFields.innerHTML = fields.map(renderField).join("");
}

function renderField(field) {
    if (field.source) {
        const options = state.data[field.source].map((item) => {
            return `<option value="${item.id}">${escapeHtml(getName(item))}</option>`;
        }).join("");

        return `
            <div class="field">
                <label for="${field.name}">${field.label}</label>
                <select id="${field.name}" name="${field.name}" required>
                    <option value="">Selecione</option>
                    ${options}
                </select>
            </div>
        `;
    }

    return `
        <div class="field">
            <label for="${field.name}">${field.label}</label>
            <input id="${field.name}" name="${field.name}" type="${field.type || "text"}" placeholder="${field.placeholder}" required>
        </div>
    `;
}

function renderTable() {
    const records = filteredRecords();
    const columns = [
        {
            label: "Registro",
            render: (item) => `<strong>${escapeHtml(getName(item))}</strong><span>#${item.id}</span>`
        },
        ...(currentModule().columns || []).map((column) => ({
            label: column.label,
            render: (item) => escapeHtml(displayValue(getPath(item, column.path)))
        }))
    ];

    elements.resultCount.textContent = `${records.length} ${records.length === 1 ? "item" : "itens"}`;
    elements.tableHead.innerHTML = `
        <tr>
            ${columns.map((column) => `<th>${column.label}</th>`).join("")}
            <th>Acoes</th>
        </tr>
    `;

    elements.tableBody.innerHTML = records.length
        ? records.map((record) => rowMarkup(record, columns)).join("")
        : `<tr><td class="empty-cell" colspan="${columns.length + 1}">Nenhum registro encontrado</td></tr>`;
}

function rowMarkup(record, columns) {
    return `
        <tr>
            ${columns.map((column) => `<td>${column.render(record)}</td>`).join("")}
            <td>
                <div class="actions">
                    <button class="table-action" type="button" data-action="edit" data-id="${record.id}" title="Editar">
                        <i data-lucide="pencil" aria-hidden="true"></i>
                    </button>
                    <button class="table-action danger" type="button" data-action="delete" data-id="${record.id}" title="Remover">
                        <i data-lucide="trash-2" aria-hidden="true"></i>
                    </button>
                </div>
            </td>
        </tr>
    `;
}

function filteredRecords() {
    const term = state.search.trim().toLowerCase();

    return state.data[state.view].filter((record) => {
        return !term || [
            record.id,
            record.nome,
            record.email,
            getName(record.areaConhecimento),
            getName(record.coordenador),
            getName(record.curso)
        ].filter(Boolean).join(" ").toLowerCase().includes(term);
    });
}

async function saveRecord(event) {
    event.preventDefault();

    const id = state.editingId;
    const module = currentModule();
    const url = id ? `${module.endpoint}/${id}` : module.endpoint;

    try {
        await requestJson(url, {
            method: id ? "PUT" : "POST",
            body: JSON.stringify(buildPayload(new FormData(elements.entityForm)))
        });

        clearForm();
        await loadAll();
        setFeedback(id ? "Registro atualizado." : "Registro cadastrado.", "success");
    } catch (error) {
        setFeedback(error.message, "error");
    }
}

function buildPayload(formData) {
    return Object.fromEntries(currentModule().fields.map((field) => {
        const value = formData.get(field.name);
        return [field.name, field.source ? Number(value) : value?.trim()];
    }));
}

function editRecord(id) {
    const record = state.data[state.view].find((item) => item.id === id);

    if (!record) {
        return;
    }

    state.editingId = id;
    renderForm();

    currentModule().fields.forEach((field) => {
        setFieldValue(field.name, field.editPath ? getPath(record, field.editPath) : record[field.name]);
    });

    setFeedback("", "");
    $("#nome")?.focus();
    refreshIcons();
}

async function deleteRecord(id) {
    const record = state.data[state.view].find((item) => item.id === id);

    if (!record || !window.confirm(`Remover ${getName(record)}?`)) {
        return;
    }

    try {
        await requestJson(`${currentModule().endpoint}/${id}`, { method: "DELETE" });
        clearForm();
        await loadAll();
        setFeedback("Registro removido.", "success");
    } catch (error) {
        setFeedback(error.message, "error");
    }
}

function clearForm() {
    state.editingId = null;
    elements.entityForm.reset();
    renderForm();
}

function setView(view) {
    state.view = view;
    state.search = "";
    elements.searchInput.value = "";
    clearForm();
    render();
}

function setFieldValue(name, value) {
    const field = elements.entityForm.elements[name];

    if (field) {
        field.value = value ?? "";
    }
}

function currentModule() {
    return modules[state.view];
}

function sortByName(items) {
    return [...items].sort((a, b) => getName(a).localeCompare(getName(b), "pt-BR"));
}

function getName(item) {
    return item?.nome || "-";
}

function getPath(item, path) {
    return path.split(".").reduce((value, key) => value?.[key], item);
}

function displayValue(value) {
    return typeof value === "object" ? getName(value) : value || "-";
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function setFeedback(message, type) {
    elements.feedback.textContent = message;
    elements.feedback.className = `feedback ${type}`.trim();
}

function setStatus(message) {
    elements.statusText.textContent = message;
}

function refreshIcons() {
    if (window.lucide) {
        window.lucide.createIcons();
    }
}

elements.navButtons.forEach((button) => {
    button.addEventListener("click", () => setView(button.dataset.view));
});

elements.refreshButton.addEventListener("click", loadAll);
elements.entityForm.addEventListener("submit", saveRecord);
elements.cancelEditButton.addEventListener("click", clearForm);
elements.searchInput.addEventListener("input", (event) => {
    state.search = event.target.value;
    renderTable();
    refreshIcons();
});

elements.tableBody.addEventListener("click", (event) => {
    const button = event.target.closest("button[data-action]");

    if (!button) {
        return;
    }

    const id = Number(button.dataset.id);

    if (button.dataset.action === "edit") {
        editRecord(id);
    } else if (button.dataset.action === "delete") {
        deleteRecord(id);
    }
});

document.addEventListener("DOMContentLoaded", () => {
    render();
    loadAll();
});
