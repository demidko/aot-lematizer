### aot-lematizer -- Лемматизатор

#### Описание. Библиотека принимает на вход слово и выдает:
* Все возможные леммы этого слова.
* Вдобавок каждой лемме идёт набор грамматической информации,  
   который может соответствовать этому слову.  
* Например: "дорогой":
   * *исходная лемма*: дорога,  
   *часть речи*: существительное (кто?),  
   *падеж*: творительный(чем?),  
   *единственное число*
   * *исходная лемма*: дорогой,  
   *часть речи*: прилагательное (какой?)
   *падеж*: дательный (кому?) или предложный (о ком?) или творительный (кем?)  
   *единственное число*

#### Пример простого использования.

```
var flexionStorage = new FlexionStorage();
var flexions = flexionStorage.get("дорога");
for(var flexion: flexions) {

  // исходная лемма из которой было образовано слово
  var sourceLemmaString = flexion.lemma;

  // массив грамматической информации слова
  // (если применить ее к лемме, по правилам русского языка получается искомое слово "дорога")
  var grammarInfoArray = flexion.grammarInfo;
  
  // выводим на экран лемму
  System.out.println(lemma);
  // делаем разные действия в зависимости от грамматических характеристик слова
  for(var inf: grammarInfoArray) {
    case GrammarInfo.Noun:
      // *** //
    case GrammarInfo.ShortAdjective:
      // *** //
    default: 
      // *** //
  }
  
}
```
   
 
### Полезные ссылки:
* [Описание исходного формата словаря, из которого взяты слова](http://phpmorphy.sourceforge.net/dokuwiki/manual-graminfo)  
* [Аббревиатуры, использованые в перечислении GrammarInfo](https://sourceforge.net/p/seman/svn/HEAD/tree/trunk/Docs/Morph_UNIX.txt)
