<strong>JAVA 漫步</string>

<strong>工程说明 [为了方便演示，所有项目都整合在springboot中]</string><br/>
(1)stroll-complier-dust：自制脚本语言解释器 [支持中英文脚本编程]<br/>
<p style="padding-left:20px;">
--在eclipse或ide中运行com.jrj.stroll.complier.dust.DustApp，或打包运行<br/>
--在浏览器输入：http://localhost:8080/stroll/complier-dust/parser<br/>
--中文语法分析器 : com.jrj.stroll.complier.dust.parser.ChnParser<br/>
--中文语法解释器 : com.jrj.stroll.complier.dust.calc.ChnEvaluator<br/>
在左边框输入代码<br/>
演示-：while 和 if .. else ..<br/>
<pre>
	奇数 取值 0
	偶数 = 0
	变量1 = 1
	当满足条件(变量1 小于 10)循环执行
	{
		如果(变量1 模 2 等于 0)那么 {
			奇数 = 奇数 + 变量1
		} 否则 {
			偶数 = 偶数 + 变量1
		}
		变量1 = 变量1 加 1
	}
	结果 = 奇数 + 偶数
</pre>
<br>
演示二：function <br/>
<pre>
	定义 计算斐波那契数(n)
	{
		如果(n 小于 2)那么{
			n
		}否则{
			计算斐波那契数(n-1) 加 计算斐波那契数(n-2)
		}
	}
	计算斐波那契数(11)
</pre>
<br/>
演示三：switch case <br/>
<pre>
	定义 方法一(谁)
	{
		根据条件(谁)选择
		{
			当条件等于(1)执行
			{
				"123"
			}
			当条件等于(2)执行
			{
				3
			}
			当条件等于(3)执行
			{
				2
			}
			当条件等于("123")执行
			{
				1
			}
		}
	}
	
	定义 方法二(谁)
	{
		谁
	}
	
	选择条件 = "123"
	根据条件 (方法一(选择条件)) 选择
	{
		当条件等于 (1) 执行
		{
			方法二("张三")
		}
		当条件等于 (2) 执行
		{
			方法二("李四")
		}
		当条件等于 ("123") 执行
		{
			方法二("王五")
		}
		缺省执行
		{
			"谁也没选"
		}
	}
</pre>
<br>
然后点击下面的"语义分析(CH)"和"执行(CH)"<br/>
右边框显执行式结果。<br/>
</p>
(2)stroll-rpc: RPC
