/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.visao.beans;

import br.ufg.inf.fabrica.sempreufg.controller.BannerController;
import br.ufg.inf.fabrica.sempreufg.dominio.Banner;
import br.ufg.inf.fabrica.sempreufg.dominio.ConfiguracaoDePastas;
import br.ufg.inf.fabrica.sempreufg.visao.util.MensagensTela;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author auf
 */
@Named(value = "bannerBean")
@SessionScoped
public class BannerBean implements Serializable {

    private Banner bannerEdicao;
    private List<Banner> banners;
    private final BannerController controller;
    private final MensagensTela mensagemDeTela = new MensagensTela();
    private StreamedContent imageStream;

    public BannerBean() {
        controller = new BannerController();
        this.banners = controller.consultar("", 0);
    }

    public Banner getBannerEdicao() {
        return bannerEdicao;
    }

    public void setBannerEdicao(Banner banner) {
        this.bannerEdicao = banner;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    public StreamedContent getImageStream() {
        return this.getBannerEdicao().getPathImagem() != null ? stringToStreamedContent(this.getBannerEdicao().getPathImagem()) : null;
    }
   
    public String atualizarBanner() {
        Date agora = new Date();
        bannerEdicao.setTimeStampCriacao(agora);
        List<String> inconsistencias = controller.atualizar(bannerEdicao);
        if (inconsistencias.isEmpty()) {
            mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Banner atualizado com sucesso", "");
            //atualizar lista de banners conforme o banco
            this.setBanners(controller.buscarTodos());
            return "/admin/listagemBanners?faces-redirect=true";
        }
        for (String inconsistencia : inconsistencias) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(inconsistencia));
        }
        return "";
    }

    public String excluirBanner() {
        if (this.controller.excluirBanner(bannerEdicao)) {
            File fileApagar = new File(this.bannerEdicao.getPathImagem());
            fileApagar.delete();
            this.getBanners().remove(bannerEdicao);
            mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Banner excluído com sucesso", "");
        } else {
            mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Banner não podê ser excluído!", "");
        }
        return "/admin/listagemBanners?faces-redirect=true";
    }

    public String editarBanner(Banner banner) {
        if (banner == null) {
            this.bannerEdicao = new Banner();
        } else {
            this.bannerEdicao = banner;
        }
        return "/admin/editarBanner?faces-redirect=true";
    }

    public String cancelarEdicaoBanner() {
        this.bannerEdicao = null;
        return "/admin/listagemBanners?faces-redirect=true";
    }

    public String carregarImagem(FileUploadEvent event) throws FileNotFoundException {
        UploadedFile uFile = event.getFile();
        String caminhoCompletoArquivo = ConfiguracaoDePastas.getPASTA_BANNERS() + uFile.getFileName();

        try {
            File diretorioBanners = new File(ConfiguracaoDePastas.getPASTA_BANNERS());
            if (!diretorioBanners.exists()) {
                diretorioBanners.mkdir();
            }
            File file = new File(caminhoCompletoArquivo);
            try (OutputStream out = new FileOutputStream(file)) {
                out.write(uFile.getContents());
            }
            BufferedImage img = ImageIO.read(file);
            int largura = img.getWidth();
            int altura = img.getHeight();

            if (largura > altura * 4.4 || altura > largura * .27) {
                mensagemDeTela.criar(FacesMessage.SEVERITY_ERROR, "Dimensões da imagem não está na proporção 4x1!", "/admin/editarBanner.xhtml");
                System.out.println(file.getAbsolutePath());
                file.delete();
                return "/admin/editarBanner.xhtml";
            } else if (this.bannerEdicao.getPathImagem() != null) {
                File fileApagar = new File(this.bannerEdicao.getPathImagem());
                fileApagar.delete();
            }
            this.bannerEdicao.setPathImagem(caminhoCompletoArquivo);            
            return "";
        } catch (IOException ex) {
            mensagemDeTela.criar(FacesMessage.SEVERITY_ERROR, "Erro upload imagem banner!", "/admin/editarBanner.xhtml");
            Logger.getLogger(EgressoBean.class.getName()).log(Level.SEVERE, "Erro upload imagem banner!", ex);
            return "/admin/editarBanner.xhtml";
        }
    }

    private StreamedContent stringToStreamedContent(String caminhoArquivo) {
        try {
            FileInputStream stream = new FileInputStream(caminhoArquivo);
            return new DefaultStreamedContent(stream, "application/octet-stream", caminhoArquivo);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(EgressoBean.class
                    .getName()).log(Level.SEVERE, null, "");
            return null;
        }
    }

}
