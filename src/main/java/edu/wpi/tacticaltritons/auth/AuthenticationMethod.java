package edu.wpi.tacticaltritons.auth;

import java.util.Arrays;
import java.util.Locale;

public enum AuthenticationMethod {
    EMAIL,
    APP,
    PHONE;

    public String formalName(){
        return this.name().charAt(0) + this.name().toLowerCase(Locale.ROOT).substring(1);
    }
    public static AuthenticationMethod parseAuthenticationMethod(String authMethod){
        if(authMethod == null) return null;
        authMethod = authMethod.toUpperCase(Locale.ROOT);
        if(authMethod.equals(EMAIL.name())) return EMAIL;
        else if(authMethod.equals(APP.name())) return APP;
        else if(authMethod.equals(PHONE.name())) return PHONE;
        return null;
    }
    public static String[] compileMethods(String[] methods, String methodToFront){
        if(methods != null) {
            String[] ret;
            if (Arrays.asList(methods).contains(methodToFront)) {
                ret = new String[methods.length];
                ret[0] = methodToFront;
                for(int i = 0, j = 1; i < methods.length; i++){
                    if(methods[i].equals(methodToFront)) continue;
                    methods[j++] = methods[i];
                }
            } else {
                ret = new String[methods.length + 1];
                ret[0] = methodToFront;
                System.arraycopy(methods, 0, ret, 1, methods.length);
            }
            return ret;
        }
        return new String[]{methodToFront};
    }
    public static String[] removeMethod(String[] methods, String methodToRemove){
        if(methods != null){
            if(Arrays.asList(methods).contains(methodToRemove)){
                String[] ret = new String[methods.length - 1];
                for(int i = 0, j = 0; i < methods.length; i++){
                    if(methods[i].equals(methodToRemove)) continue;
                    ret[j++] = methods[i];
                }
                return ret;
            }
            return methods;
        }
        return null;
    }
}
