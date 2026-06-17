-- Réinitialise la séquence auto-incrémentée après des inserts manuels ou imports.
-- À exécuter si erreur : duplicate key value violates unique constraint "categorieproduit_pkey"

SELECT setval(
    pg_get_serial_sequence('categorieproduit', 'idcategorie_produit'),
    COALESCE((SELECT MAX(idcategorie_produit) FROM categorieproduit), 0) + 1,
    false
);
