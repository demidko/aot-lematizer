## aot-lematizer -- Библиотека морфологического анализа на Java

### Решаемые задачи.
* Лемматизация (получение нормальной формы слова)
* Получение грамматической информации для слова (часть речи, падеж, спряжение и т.д.)

### Краткое описание.
Библиотека принимает на вход форму слова и возвращает набор пар вида:  
* Лемма (нормальная форма слова)
* Грамматическая информация о переданной форме слова, в соответствии с леммой

Например, рассмотрим слово "дорогой". Есть (как минимум) два варианта:
* *исходная лемма*: дорога,  
  *часть речи*: существительное (кто?),  
  *падеж*: творительный(чем?),  
  *единственное число*
* *исходная лемма*: дорогая,  
  *часть речи*: прилагательное (какой?)  
  *падеж*: дательный (кому?) или предложный (о ком?) или творительный (кем?)  
  *единственное число*

### Пример использования в коде.

```
var flexionStorage = new FlexionStorage();
var flexions = flexionStorage.get("дорога");
for(var flexion: flexions) {

  // исходная лемма из которой было образовано слово
  var sourceLemmaString = flexion.lemma;

  // массив грамматической информации слова
  // (если применить ее к лемме, 
  //  по правилам русского языка получается искомое слово "дорога")
  var grammarInfoArray = flexion.grammarInfo;
  
  // выводим на экран лемму
  System.out.println(lemma);
  // делаем разные действия 
  // в зависимости от грамматических характеристик слова
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
* [Аббревиатуры использованые в перечислении GrammarInfo](http://phpmorphy.sourceforge.net/dokuwiki/manual-graminfo)  
* [Описание исходного формата словаря](https://sourceforge.net/p/seman/svn/HEAD/tree/trunk/Docs/Morph_UNIX.txt)
* [Консольное приложение использующее aot-lemmatizer](https://github.com/demidko/aot-lematizer/blob/master/testapp/src/main/java/com/farpost/aot/TestApplication.java)
 
