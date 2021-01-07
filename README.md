# Pascal-Cplus

---

## SETUP
1. git clone https://github.com/reFreshD2/Pascal-Cplus.git
2. Open Project in ApacheNetBeans
3. Run Project
4. PROFIT!

---

*src/LexAnalyzer* - класс лексического анализатора Pascal
  + LexAnalyzer(String fileName) - конструктор создания класса с указанием названия считываемого файла
  + void makeAnalysis() - метод формирования списка лексем из считываемого файла
  + void print() - метод вывода списа лексем на экран
  + ArrayList<Pair> getListLexem() - возвращает список лексем, полученый при анализе
  
*src/Pair* - класс лексемы - пара <name,value>
  + Pair(String type, String name) - конструктор лексемы, принимающий на вход тип и имя лексемы
  + Pair(String type, String name, int numString) - конструктор класса лексемы, принимающий на вход тип, имя и номер строки лексемы
  + String getName() - возвращает name Pair
  + String getType() - возвращает type Pair
  + Pair copy() - возвращает копию объекта Pair
  + boolean equals(Object o) - сравниваем текущую лексему с объектом
  + void print() - метод вывода лексемы на экран
  + void setContextType(String type) - процедура установки контектного типа лексемы
  + String getContextType() - функция, возвращающая контекстный тип лексемы
  + int getNumString() - функция, возвращающая номер строки лексемы
  + boolean equals(Object o) - перегрузка метода сравнения
  + String toString() - перегрузка метода преобразования объекта в строку
  + void setContextValue(String value) - процедура установки контекстного значения лексемы
  + String getContextValue() - функция, возвращающая контекстное значение лексемы
  + void setInUse(boolean value) - процедура установки значения "Использовалась ли лексема"
  + boolean getInUse() - функция, возвращающая значение "Использовалась ли лексема"
 
*src/ParseTree* - класс дерева - <TreeItem root>
  + ParseTree(Pair val) - конструктор дерева
  + TreeItem getRoot() - возвращает TreeItem - корень дерева
  
*src/TreeItem* - класс узла дерева разбора
  + TreeItem(Pair pair) - конструктор класса, атом разбора [nterm/term]
  + void setParent(TreeItem par) - процедура установки родителя узла
  + void addChilds(ArrayList<Pair> list) - процедура добавления детей узла
  + ArrayList<TreeItem> getChilds() - функция, возвращающая детей узла
  + TreeItem getParent() - функция, возвращающа родителя узла
  + Pair getVal() - функция, возвращающая значение узла
  
*src/Rule* - класс правило грамматики - <Pair left, ArrayList<Pair> right> === [left -> right]
  + Rule(Pair left, ArrayList<Pair> right) - конструктор правила
  + Rule copy() - возвращает копию объекта Rule
  + Pair getLeft() - возвращает левую часть правила
  + ArrayList<Pair> getRight() - возвращает правую часть правила
  + Pair getPair(int pos) - возвращает Pair из правой части правила по позиции pos
  + int getPosSymbol(Pair symbol) - возвращает номер Pair, равный symbol, из правой части правила, в случае, если нет такого символа возвращает -1
  + Rule getRuleWithDot(int pos) - возвращает копию правила с символом $ в позиции pos
  + void print() - метод вывода правила на экран
  + Pair swap(int left, int right) - возвращает текущее правило со сменой позиций правила в позиции left и right
  + void setRight(ArrayList<Pair> list) - процедура установки правой части правила
  + boolean rightEquals(ArrayList<Pair> pairs) - функция, проверяющая на равенство правых частей правил
  
*src/Situation* - класс ситуации алгоритма Эрли <Rule rule, int pos>
  + Situation(Rule rule, int pos) - конструктор класса Situation
  + boolean equals(Object o) - сравниваем текущую ситуацию с объектом
  + int getPos() - возвращает pos
  + Rule getRule() - возвращает rule
  + void print() - метод вывода ситуации на экран
  + void setIsProcessedEnd(boolean isProcessed) - процедура установки значения "Использовалась ли ситуация при поиски ситуаций с точкой на конце"
  + boolean getIsProcessedEnd() - функция, возвращающая значение "Использовалась ли ситуация при поиски ситуаций с точкой на конце"
  + void setIsProcessedAtFront(boolean isProcessed) - процедура установки значения "Использовалась ли ситуация при поиски ситуаций с точкой перед нетерминалом"
  + boolean getIsProcessedAtFront() - функция, возвращающая значение "Использовалась ли ситуация при поиски ситуаций с точкой перед нетерминалом"
  
*src/GrammarInterface* - интерфейс класса грамматик
  + void fillRules() - процедура наполнение грамматики правилами
  + void print() - метод вывода грамматики на экран
  + Pair getAxiom() - возвращает аксиому грамматики
  + ArrayList<Rule> getRules(Pair left) - возвращает правила граматики по левой части правила
  + Rule getRuleByIndex(int index) - возвращает правило по индексу

*src/PascalGrammar* implements GrammarInterface - класс грамматики Pascal

*src/CGrammar* implements GrammarInterface - класс грамматики C++

*src/SynAnalyzer* - класс синтаксического анализатора, основанный на методе Эрли
  + SynAnalyzer(ArrayList<Pair> lexems, GrammarInterface grammar) - конструктор класса SynAnalyzer
  + void makeTable() - процедура составления таблицы разбора для метода Эрли
  + void printTable() - процедура печати таблицы разбора на экран
  + void parse() - процедура построения цепочки разбора
  + void buildTree() - процедура посроения дерева разбора
  + ParseTree getTree() - возвращает дерево после синтаксического разбора
  + ArrayList<Integer> getParse() - возвращает цепочку разбора
  
*src/PossibleOperation* - класс возможных операций в Pascal
  + PossibleOperation(String operation, String typeOfOperand, String returnType) - конструктор унарной операции принимающий символ операции, тип аргумента и тип возвращаемого значения
  + PossibleOperation(String operation, String typeOfOperand1, String typeOfOperand2, String returnType) - конструктор бинарной операции принимающий символ операции, типы аргументов и тип возвращаемого значения
  + String getReturnType() - функция, возвращающая тип возвращаемый операцией
  + boolean equals(Object o) - перегрузка функции сравнения операций
  
*src/PossibleOperationRepository* - класс контейнер всех возможных операций в Pascal
  + PossibleOperationRepository() - конструктор класса
  + String getReturnType(String operation, String operandType1, String operandType2) - функция поиска бинарной операции
  + public String getReturnType(String operation, String operandType) - функция поиска унарной операции
  
*src/SemAnalyzer* - класс семантического анализатора
  + SemAnalyzer(ParseTree tree) - конструктор класса, принимающий на вход дерево разбора
  + void makeAnalysis() - процедура проверки контекстных условий
  + boolean hasError() - функция результата семантического анализа

*src/Translator* - класс траслятор из Pascal в C++
  + Translator(GrammarInterface grammar, ArrayList<Integer> parseString, ArrayList<Pair> lexems) - конструктор класса, принимающий на вход грамматику, список разбора и список лексем
  + void translate(String filename) - процедура транслирования, результат записывается в файл filename
  
*src/Util* - класс приложения транслятора Pascal - C++

Пример считывываемых файлов "program{i}.txt" находится в корне проекта
Пример выходного файла "program.cpp" находится в корне проекта
