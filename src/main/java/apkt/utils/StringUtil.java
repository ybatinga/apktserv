package apkt.utils;

import apkt.service.ProjService;

public class StringUtil {
    
    public static void setLanguageResource(String language) throws Exception{
        
        if (language.equals("pt")){
                ProjService.RB = ProjService.LanguegeType.resourceBundle_pt;
        } else {
                ProjService.RB = ProjService.LanguegeType.resourceBundle_default;
        } 
    }
    
}
