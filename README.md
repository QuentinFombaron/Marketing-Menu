# Marking-Menu

## AUTHEUR :    

Quentin FOMBARON   
Léo VALETTE  
  
## CHOIX DE CONCEPTION
   
Nous avons opté pour un Marking Menu sous forme de "Camembert" à plusieurs niveaux.
Ce système nous semble plus ergonomique tant pour un utilisateur novice qu'expert.
Il apporte aussi un avantage visuel et une utilisation intuitive. 
Pour **ouvrir** le menu, cliquer et maintenir le **clic droit** de la souris. 
Déplacer le curseur dans les différents menus souhaités. 
Si un sous-menu est disponible, ce dernier s'affichera lorsque le curseur sortira du menu principal.
Le choix est effectué lorsque le clic droit est **relâché**. 
Cette solution apporte plusieurs manières d'annuler une action, soit en relâchant le clic sur le menu ```Cancel```.
Soit, pour l'utilisateur expert, en relâchant le clic cette fois sur n'importe quel item du menu principal.

## BUGS ET SITUATIONS NON GÉRÉES

- Lorsqu'un outil et une couleur sont sélectionnés, un double clic droit entraine une Exception de sortie de tableau sur l'objet ```Paint```.
- Nous n'avons pas géré le cas où le clic droit est effectué en bordure de fenêtre.
- Dans le cas où le texte affiché dans le menu est trop grand, ce dernier peut dépasser de sa zone. Nous avons essyé de manipuler la méthode ```g.rotate()``` mais sans succès. 
- Dans notre manière d'ajouter les formes dans le tableau ```shapes```, cela ralenti grandement les perfermances lorsque beaucoup de formes sont dessinées.

## NOTE

La gestion des modifications des menus est modulaire (nombre d'items dans le menu principal et sous-menu, label des items).