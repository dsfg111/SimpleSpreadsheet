# 开发日志
## 2026-06-15
**进度**: 添加了对于 WorksheetBuilder 的实现, 完全测试了 WorksheetReader  
**设计**: WorksheetReader 只负责将单元格和字符串内容分离, 对于标准公式以=开头, 
例如"=(SUM 1 1)", Parser 只解析括号公式, 即"(SUM 1 1)", 如果直接将这个字符串内容
向下传递会在 Parser 时出错, 所以在自行构建的 BasicWorksheetBuilder 中对于接口函数
public WorksheetBuilder<SpreadsheetModel> createCell(int col, int row, String contents)
的实现中要将 contents 参数的开头 = 去除再向下传递.

## 2026-06-12
**进度**: 对于单元格公式求值添加了缓存。新增函数 SUB(减), SQRT(开平方)


## 2026-06-07
**进度**: 检测禁止公式间的直接或间接引用  
**修复**: 解决了之前对于单元格矩形范围引用无法参加函数值计算的问题  
**设计**: 引入一个 dfs 的递归求值辅助函数，用一个集合追踪当前正在使用的单元格


## 2026-06-05
**进度**: 
- 添加 PRODUCT(相乘), <(小于), CONCAT(拼接字符串) 函数
- 规范了抛出异常，在模型 evaluate 函数中将 FormulaEval 的 IllegalArgumentException 转换为 IllegalStateException

**待办**: 检测禁止公式间的直接或间接引用


## 2026-06-04
- **进度**: 引入 Formula 重构模型求值逻辑
- **设计**:  
原始字符串(String) 由 Parser 解析为 S表达式(Sexp), 再由 FormulaEval 解析为 公式(Formula)
表格模型对单元格进行估值时由内部对 Formula 的访问器来进行, 估值的结果为 值(Value).
    

## 2026-06-02
- **进度**: 基本实现了 model 的接口功能
- **未完成**: 
  1. 目前只实现了 SUM 函数，其他函数功能待实现
  2. 未实现检测并禁止公式之间的循环引用
- **问题**:  
  当前实现极其混乱，所有的求值逻辑都在 EvalVisitor 类中。 
  类内部包括了对于符号和公式的解析， 
  并且将 "A1:B1" 这类矩形引用直接解为 RegionValue 一个对 Value 列表的封装。 
  并且还有一个内部的实现 Sexp 访问器来获取符号的字符串，作为函数名。 
  对于函数使用switch一一比对函数名，极其不优雅。
  当前对于 Value 接口的封装设计存疑，它就是对基本值类型的简单封装，
  但对于单元格中不同的值统一为 Value 这一个，不然 evaluate 函数没法同时返回 double, boolean, string 三种数据类型。
  不过在实际上这些在表格中统一显示未字符串，区分数据类型，是为公式计算提供可能。
  即使实际使用接口中对于输入 1 或 "1", 他们在对于设置和获取单元格内容的接口其实只需要原样返回字符串内容就行了，
  但是对于 SUM 这个公式来说 1 是数字，需要在结果上加一，而 "1" 是字符串，无法参与计算，需要另外处理(忽略/报错).
  以及对于 Value 也是使用访问者模式，当前的 SUM 函数就是对 ValueVisitor 的实现来正确区别和取值 Value.
- **待办**:  
  考虑引入一个 formula 中间层数据接口，表示作业要求中的公式，
  使用这个来处理对于符号和区域引用和函数的表示，再对于基本 Value 进行一个上层封装。
  这样访问器返回一个 Formula 中间层数据，再对其进行访问器模式或其他的形式，最终转变为 Value。
- **当前数据流设想**:  
  RawString -> Sexp -> Formula -> Value


  

