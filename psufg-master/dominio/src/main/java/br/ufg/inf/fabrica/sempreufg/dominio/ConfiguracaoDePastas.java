package br.ufg.inf.fabrica.sempreufg.dominio;

import java.io.File;

/**
 *
 * @author auf
 */
public class ConfiguracaoDePastas {

    private static String PASTA_FOTOS = "";
    private static String PASTA_BANNERS = "";

    public static String getPASTA_FOTOS() {
        String linux = "/usr/tmp/fotos/";
        String windows = "C:\\Users\\Public\\fotos\\";

        if (new File("linux").exists()) {
            setPASTA_FOTOS(linux);
        } else {
            setPASTA_FOTOS(windows);
        }
        return PASTA_FOTOS;
    }
    
    public static String getPASTA_BANNERS() {
        String linux = "/usr/tmp/banners/";
        String windows = "C:\\Users\\Public\\banners\\";

        if (new File("linux").exists()) {
            setPASTA_BANNERS(linux);
        } else {
            setPASTA_BANNERS(windows);
        }
        return PASTA_BANNERS;
    }

    public static void setPASTA_FOTOS(String PASTA_FOTOS) {
        ConfiguracaoDePastas.PASTA_FOTOS = PASTA_FOTOS;
    }
    
    public static void setPASTA_BANNERS(String PASTA_BANNERS) {
        ConfiguracaoDePastas.PASTA_BANNERS = PASTA_BANNERS;
    }

   }
