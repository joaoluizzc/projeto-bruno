const API_URL = "/api/professores";

const tabela = document.querySelector("#tabelaProfessores");
const contador = document.querySelector("#contador");
const mensagem = document.querySelector("#mensagem");

const formCadastro = document.querySelector("#formCadastro");
const formEdicao = document.querySelector("#formEdicao");
const formDelete = document.querySelector("#formDelete");

const btnAtualizar = document.querySelector("#btnAtualizar");
const btnBuscarEdicao = document.querySelector("#btnBuscarEdicao");
const btnBuscarDelete = document.querySelector("#btnBuscarDelete");
const btnExcluir = document.querySelector("#btnExcluir");

const idEdicao = document.querySelector("#idEdicao");
const nomeEdicao = document.querySelector("#nomeEdicao");
const emailEdicao = document.querySelector("#emailEdicao");
const materiaEdicao = document.querySelector("#materiaEdicao");

const idDelete = document.querySelector("#idDelete");
const dadosDelete = document.querySelector("#dadosDelete");

let professorParaExcluir = null;

function mostrarMensagem(texto, tipo = "success") {
    mensagem.textContent = texto;
    mensagem.className = `message show ${tipo}`;
}

function limparMensagem() {
    mensagem.textContent = "";
    mensagem.className = "message";
}

async function respostaJson(response) {
    const texto = await response.text();
    return texto ? JSON.parse(texto) : null;
}

async function requisicao(url, opcoes = {}) {
    const response = await fetch(url, {
        headers: {
            "Content-Type": "application/json",
            ...(opcoes.headers || {})
        },
        ...opcoes
    });

    const dados = await respostaJson(response);

    if (!response.ok) {
        throw new Error(dados?.message || "Nao foi possivel concluir a operacao");
    }

    return dados;
}

function lerCadastro() {
    return {
        nome: document.querySelector("#nomeCadastro").value,
        email: document.querySelector("#emailCadastro").value,
        materia: document.querySelector("#materiaCadastro").value
    };
}

function lerEdicao() {
    return {
        nome: nomeEdicao.value,
        email: emailEdicao.value,
        materia: materiaEdicao.value
    };
}

function preencherEdicao(professor) {
    idEdicao.value = professor.id;
    nomeEdicao.value = professor.nome;
    emailEdicao.value = professor.email;
    materiaEdicao.value = professor.materia;
}

function preencherDelete(professor) {
    professorParaExcluir = professor;
    dadosDelete.innerHTML = `
        <strong>${professor.nome}</strong>
        Email: ${professor.email}<br>
        Materia: ${professor.materia}
    `;
    btnExcluir.disabled = false;
}

function limparDelete() {
    professorParaExcluir = null;
    dadosDelete.textContent = "Informe um ID para localizar o professor.";
    btnExcluir.disabled = true;
}

function renderizarTabela(professores) {
    contador.textContent = `${professores.length} registro${professores.length === 1 ? "" : "s"}`;

    if (professores.length === 0) {
        tabela.innerHTML = `<tr><td class="empty" colspan="5">Nenhum professor cadastrado.</td></tr>`;
        return;
    }

    tabela.innerHTML = professores.map(professor => `
        <tr>
            <td>${professor.id}</td>
            <td>${professor.nome}</td>
            <td>${professor.email}</td>
            <td>${professor.materia}</td>
            <td class="actions">
                <button class="link-button" type="button" data-editar="${professor.id}">Editar</button>
                <button class="link-button" type="button" data-deletar="${professor.id}">Excluir</button>
            </td>
        </tr>
    `).join("");
}

async function listarProfessores() {
    const professores = await requisicao(API_URL);
    renderizarTabela(professores);
}

async function buscarProfessor(id) {
    if (!id) {
        throw new Error("Informe um ID");
    }

    return requisicao(`${API_URL}/${id}`);
}

btnAtualizar.addEventListener("click", async () => {
    try {
        limparMensagem();
        await listarProfessores();
        mostrarMensagem("Lista atualizada com sucesso.");
    } catch (error) {
        mostrarMensagem(error.message, "error");
    }
});

formCadastro.addEventListener("submit", async (event) => {
    event.preventDefault();

    try {
        limparMensagem();
        await requisicao(API_URL, {
            method: "POST",
            body: JSON.stringify(lerCadastro())
        });
        formCadastro.reset();
        await listarProfessores();
        mostrarMensagem("Professor cadastrado com sucesso.");
    } catch (error) {
        mostrarMensagem(error.message, "error");
    }
});

btnBuscarEdicao.addEventListener("click", async () => {
    try {
        limparMensagem();
        const professor = await buscarProfessor(idEdicao.value);
        preencherEdicao(professor);
        mostrarMensagem("Cadastro carregado para edicao.");
    } catch (error) {
        mostrarMensagem(error.message, "error");
    }
});

formEdicao.addEventListener("submit", async (event) => {
    event.preventDefault();

    try {
        limparMensagem();
        await requisicao(`${API_URL}/${idEdicao.value}`, {
            method: "PUT",
            body: JSON.stringify(lerEdicao())
        });
        await listarProfessores();
        mostrarMensagem("Cadastro atualizado com sucesso.");
    } catch (error) {
        mostrarMensagem(error.message, "error");
    }
});

btnBuscarDelete.addEventListener("click", async () => {
    try {
        limparMensagem();
        const professor = await buscarProfessor(idDelete.value);
        preencherDelete(professor);
        mostrarMensagem("Professor localizado para exclusao.");
    } catch (error) {
        limparDelete();
        mostrarMensagem(error.message, "error");
    }
});

formDelete.addEventListener("submit", async (event) => {
    event.preventDefault();

    try {
        limparMensagem();

        if (!professorParaExcluir || String(professorParaExcluir.id) !== String(idDelete.value)) {
            professorParaExcluir = await buscarProfessor(idDelete.value);
        }

        const confirmar = confirm(`Tem certeza que deseja excluir o professor ${professorParaExcluir.nome}?`);

        if (!confirmar) {
            mostrarMensagem("Exclusao cancelada.", "error");
            return;
        }

        await requisicao(`${API_URL}/${professorParaExcluir.id}`, {
            method: "DELETE"
        });
        limparDelete();
        formDelete.reset();
        await listarProfessores();
        mostrarMensagem("Professor excluido com sucesso.");
    } catch (error) {
        mostrarMensagem(error.message, "error");
    }
});

tabela.addEventListener("click", async (event) => {
    const editarId = event.target.dataset.editar;
    const deletarId = event.target.dataset.deletar;

    try {
        if (editarId) {
            const professor = await buscarProfessor(editarId);
            preencherEdicao(professor);
            mostrarMensagem("Professor carregado para edicao.");
        }

        if (deletarId) {
            const professor = await buscarProfessor(deletarId);
            idDelete.value = professor.id;
            preencherDelete(professor);
            mostrarMensagem("Professor carregado para exclusao.");
        }
    } catch (error) {
        mostrarMensagem(error.message, "error");
    }
});

listarProfessores().catch(error => mostrarMensagem(error.message, "error"));
