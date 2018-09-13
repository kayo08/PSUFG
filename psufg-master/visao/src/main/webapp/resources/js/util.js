function ehNumero(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function exibirConfirmacao(mensagem, idBotaoClicar) {
    bootbox.confirm(mensagem, function (result) {

        if (result === true) {
            document.getElementById(idBotaoClicar).click();
        }
    });
}

function setarFocoEm(item) {
    document.getElementById(item).focus();
}

function clicarBotao(idBottom) {
    document.getElementById(idBottom).click();
}


function setHeightElement(el) {
    var B = document.body,
            H = document.documentElement,
            hei;
    if (typeof document.height !== 'undefined') {
        hei = document.height + 'px'; // For webkit browsers
    } else {
        hei = Math.max(B.scrollHeight, B.offsetHeight, H.clientHeight, H.scrollHeight, H.offsetHeight) + 'px';
    }
    document.getElementById(el).style.minHeight = hei;
    console.log('Altura do elemento pagina é', hei);
}

function ativarMenu(x) {
    if (x != "") {
        x.style.fontWeight = "bold";
        x.style.borderBottom = "3px solid #337ab7";
        x.style.borderRadius = "0px 0px 10px 10px";
        x.style.backgroundColor = "#e1e1e1";
        x.style.marginTop = "-3px";
    }
}

function ativarAbaDados(x) {    
    if (x != "") {
        x.style.color = "#fff";
        x.style.background = "#337ab7";
        x.style.borderColor = "#337ab7";
    }
}

function ativarMenuPrincipal(x) {
    x.style.backgroundColor = "#deedf7";
    x.style.color = "#fff"
    x.style.fontWeight = "bold"
    x.style.borderRadius = "20px";
//    $(x).addClass('active'); //para usar o padrão do bootstrap
}

function ativarAbaSelecionada(aba) {
    var element, name, arr;
    //remove classe menu-custom-ativo de todas as abas
    element = document.getElementById("abaDadosPessoais");
    element.className = element.className.replace(/\bmenu-custom-ativo\b/g, "");
    element = document.getElementById("abaDadosResidencia");
    element.className = element.className.replace(/\bmenu-custom-ativo\b/g, "");
    element = document.getElementById("abaEnsinoMedio");
    element.className = element.className.replace(/\bmenu-custom-ativo\b/g, "");
    element = document.getElementById("abaHistoricoUFG");
    element.className = element.className.replace(/\bmenu-custom-ativo\b/g, "");
    element = document.getElementById("abaAvaliacaoCurso");
    element.className = element.className.replace(/\bmenu-custom-ativo\b/g, "");
    element = document.getElementById("abaProgramaAcademico");
    element.className = element.className.replace(/\bmenu-custom-ativo\b/g, "");
    element = document.getElementById("abaHistoricoOutrasIES");
    element.className = element.className.replace(/\bmenu-custom-ativo\b/g, "");
    element = document.getElementById("abaAtuacaoProfissional");
    element.className = element.className.replace(/\bmenu-custom-ativo\b/g, "");
    //adiciona classe menu-custom-ativo a aba selecionada
    element = document.getElementById(aba);
    name = "menu-custom-ativo";
    arr = element.className.split(" ");
    if (arr.indexOf(name) == -1) {
        element.className += " " + name;
    }
}

function desabilitarEdicao(item, value) {
    document.getElementById(item).disabled = value;
}

$('#myCarousel').carousel({
    interval: 2500
});
$('#btnPause').click(function () {
    $('#myCarousel').carousel('pause');
});
$('#btnPlay').click(function () {
    $('#myCarousel').data('bs.carousel').options.interval = 2500;
    $('#myCarousel').carousel('cycle');
});
function updateToggles(myColumnToggler) {
    $(PF(myColumnToggler).jqId).find("li").find('> .ui-chkbox > .ui-chkbox-box').each(function () {
        var chkbox = $(this);
        if (chkbox.hasClass('ui-state-active')) {
            PF(myColumnToggler).check(chkbox);
        } else {
            PF(myColumnToggler).uncheck(chkbox);
        }
    });
}

function atualizarFotoPerfil(idFileUpload) {
    document.getElementById(idFileUpload).click();
}

function exibeMapa() {
    document.getElementById("mapa").style.display = 'block';
}

function ocultaMapa() {
    document.getElementById("mapa").style.display = 'none';
}
