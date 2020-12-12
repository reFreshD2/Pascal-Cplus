# Pascal-Cplus

SETUP
1. git clone https://github.com/reFreshD2/Pascal-Cplus.git
2. Open Project in ApacheNetBeans
3. Run Project
4. PROFIT!

src/LexAnalyzer - класс лексического анализатора Pascal
  + LexAnalyzer(String fileName) - конструктор создания класса с указанием названия считываемого файла
  + void makeAnalysis() - метод формирования списка лексем из считываемого файла
  + void print() - метод вывода списа лексем на экран
  + ArrayList<Pair> getListLexem() - возвращает список лексем, полученый при анализе
  
src/Pair - класс лексемы - пара <name,value>
  + Pair(String t, String n) - конструктор лексемы
  + String getName() - возвращает name Pair
  + String getType() - возвращает type Pair
  + Pair copy() - возвращает копию объекта Pair
  + boolean equals(Object o) - сравниваем текущую лексему с объектом
  + void print() - метод вывода лексемы на экран
 
 
  
 src/ParseTree - класс дерева - <TreeItem root>
  + ParseTree(Pair val) - конструктор дерева
  + TreeItem getRoot() - возвращает TreeItem - корень дерева
  
src/Rule - класс правило грамматики - <Pair left, ArrayList<Pair> right> === [left -> right]
  + Rule(Pair left, ArrayList<Pair> right) - конструктор правила
  + Rule copy() - возвращает копию объекта Rule
  + Pair getLeft() - возвращает левую часть правила
  + ArrayList<Pair> getRight() - возвращает правую часть правила
  + Pair getPair(int pos) - возвращает Pair из правой части правила по позиции pos
  + int getPosSymbol(Pair symbol) - возвращает номер Pair, равный symbol, из правой части правила, в случае, если нет такого символа возвращает -1
  + Rule getRuleWithDot(int pos) - возвращает копию правила с символом $ в позиции pos
  + void print() - метод вывода правила на экран
  + Pair swap(int left, int right) - возвращает текущее правило со сменой позиций правила в позиции left и right
  
src/Situation - класс ситуации алгоритма Эрли <Rule rule, int pos>
  + Situation(Rule rule, int pos) - конструктор класса Situation
  + boolean equals(Object o) - сравниваем текущую ситуацию с объектом
  + int getPos() - возвращает pos
  + Rule getRule() - возвращает rule
  + void print() - метод вывода ситуации на экран
  
src/GrammarInterface - интерфейс класса грамматик
  + void fillRules() - процедура наполнение грамматики правилами
  + void print() - метод вывода грамматики на экран
  + Pair getAxiom() - возвращает аксиому грамматики
  + ArrayList<Rule> getRules(Pair left) - возвращает правила граматики по левой части правила
  + Rule getRuleByIndex(int index) - возвращает правило по индексу

src/PascalGrammar implements GrammarInterface - класс грамматики Pascal

src/SynAnalyzer - класс синтаксического анализатора, основанный на методе Эрли
  + SynAnalyzer(ArrayList<Pair> lexems, GrammarInterface grammar) - конструктор класса SynAnalyzer
  + void makeTable() - процедура составления таблицы разбора для метода Эрли
  + void printTable() - процедура печати таблицы разбора на экран
  + void parse() - процедура построения цепочки разбора
  + void buildTree() - процедура посроения дерева разбора
  
src/Util - класс приложения транслятора Pascal - C++

Пример считывываемого файла program.txt находится в корне проекта
