## aot-lematizer -- Библиотека морфологического анализа на Java

### Решаемые задачи:
* Лемматизация (получение всех возможных начальных форм слова -- лемм)
* Получение всех возможных словоформ для каждой леммы.
* Получение грамматической информации для слова (часть речи, падеж, спряжение и т.д.)

### Описание работы:
1. Библиотека принимает на вход слово и возвращает список возможнных лемм (исходных форм) этого слова.
2. К каждой лемме прикреплён список из наборов грамматических характеристик.
3. Каждый набор грамматических характеристик соответсвует искомому слову.

Например, рассмотрим результат работы библиотеки для слова "замок":  
```
1. <замок, [С, мр, ед, им], [С, мр, ед, вн]>  
2. <замок, [С, мр, ед, им], [С, мр, ед, вн]>  
3. <замокнуть, [Г, дст, прш, мр, ед]>
```  
Мы видим три леммы.  
1. Первая лемма, это слово зАмок (строение).
Вместе с ней лежат два набора характеристик, говорящие о том, чем же является слово замок, по отношению к этой лемме.  
Это либо замок (кто?) в именительном падеже, либо замок (кого?) в винительном падеже.
2. Вторая лемма, это слово замОк (устройство для запирания дверей).   
Для нее, слово замок так же может быть либо именительным, либо винительным падежом.
3. И наконец, третья лемма, это слово замокнуть (он что сделал? он замок под дождем).  
Для нее слово замок характеризуется лишь одним набором грамматической информации.

### Особенности реализации:
* Вввиду частого взаимозаменяемого использования в речи,  
  буква ё рассматривается наравне с буквой е.

### Полезные ссылки:
* [Аббревиатуры использованые в перечислении GrammarInfo](http://phpmorphy.sourceforge.net/dokuwiki/manual-graminfo)  
* [Описание исходного формата словаря](https://sourceforge.net/p/seman/svn/HEAD/tree/trunk/Docs/Morph_UNIX.txt)
* [Консольное приложение использующее aot-lemmatizer](https://github.com/demidko/aot-lematizer/blob/master/testapp/src/main/java/com/farpost/aot/TestApplication.java)

### Формат бинарного файла
```
количество морфологий
морфология
...
морфология 

количество строк
строка
...
строка

количество лемм
(индекс строки, индекс морфологии) (индекс строки, индекс морфологии)... (индекс строки, индекс морфологии) (индекс строки, индекс морфологии)
(индекс строки, индекс морфологии) (индекс строки, индекс морфологии)... (индекс строки, индекс морфологии) (индекс строки, индекс морфологии)
...
(индекс строки, индекс морфологии) (индекс строки, индекс морфологии)... (индекс строки, индекс морфологии) (индекс строки, индекс морфологии)

количество хешей (коллизии проверяются в рантайме, нет смысла отделяеть их во время компиляции, т. к. могут быть и внешние коллизии)
хеш, индекс леммы, индекс леммы
хеш, индекс леммы, индекс леммы, индекс леммы
хеш, индекс леммы, индекс леммы, индекс леммы, индекс леммы
...
хеш, индекс леммы, индекс леммы, индекс леммы
```

  
  
