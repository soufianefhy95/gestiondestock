package com.soufiane.gestiondestock.exception;

public enum ErrorCodes {

    ARTICLE_NOT_FOUND(1000),
    ARTICLE_NOT_VALID(1001),
    CATEGORY_NOT_VALID(2000),
    CATEGORY_NOT_FOUND(2001),
    // TODO: complete the rest of error codes
    CLIENT_NOT_FOUND(3000),
    COMMANDE_CLIENT_NOT_FOUND(4000),
    COMMANDE_CLIENT_NOT_VALID(4001),

    COMMANDE_CLIENT_NON_MODIFIABLE(4002),

    COMMANDE_FOURNISSEUR_NON_MODIFIABLE(4003),
    COMMANDE_FOURNISSEUR_NOT_FOUND(5000),
    COMMANDE_FOURNISSEUR_NOT_VALID(5001),
    ENTREPRISE_NOT_FOUND(6000),
    FOURNISSEUR_NOT_FOUND(7000),
    LIGNE_COMMANDE_CLIENT_NOT_FOUND(8000),
    LIGNE_COMMANDE_FOURNISSEUR_NOT_FOUND(9000),
    LIGNE_VENTE_NOT_FOUND(10000),
    MVT_STK_NOT_FOUND(11000),
    MVT_STK_NOT_VALID(11001),
    UTILISATEUR_NOT_FOUND(12000),
    VENTE_NOT_FOUND(13000),
    VENTE_NOT_VALID(13001),

    UPDATE_PHOTO_EXCEPTION(14000),

    BAD_CREDENTIALS(15000),

    UNKNOWN_CONTEXT(16000),
    UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID(17000);

    private int code;

    ErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
