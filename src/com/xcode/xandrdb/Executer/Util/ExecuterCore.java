package com.xcode.xandrdb.Executer.Util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xcode.xandrdb.annotation.Param;
import com.xcode.xandrdb.annotation.name;

/**
 * 所有执行器的核心技术类
 * @author 肖蕾
 */
public class ExecuterCore
{
	/**
	 * 将sql语句中的自定义，转化成真实的sql语句
	 * @param annotationValue
	 * @param method
	 * @param args
	 * @return
	 */
	public static String getNativeSQl(String annotationValue, Method method, Object args[])
	{
		String sql = annotationValue;
		Annotation annotations[][] = method.getParameterAnnotations();
		for (int i = 0; i < annotations.length; i++)
		{
			Annotation annotation[] = annotations[i];
			for (Annotation annotation2 : annotation)
			{
				if (annotation2.annotationType().equals(name.class) || annotation2.annotationType().equals(Param.class))
				{
					String name_k = "";
					if(annotation2.annotationType().equals(name.class))//如果是用name注解的 
					{
						name_k = ((name) annotation2).value();
					}else if (annotation2.annotationType().equals(Param.class)) //如果是用Param注解的
					{
						name_k = ((Param) annotation2).value();
					}
					
					Object value = args[i];
					// 判断参数类型是否是基本类型，以及他们的包装类，比如 integer
					if (value.getClass().isPrimitive() || value.getClass().getName().startsWith("java.lang"))
					{
						// #{a.[a-zA-Z]+\d*} 匹配的正则表达式
						sql = sql.replaceAll("\\#\\{ *" + name_k + " *\\}", "'" + value + "'");
						sql = sql.replaceAll("\\$\\{ *" + name_k + " *\\}", "'" + value + "'");
					} else // 参数类型不是基本类型 是自定义类型，譬如 ${User.name}
					{
						Map<String, String> names = new HashMap<String, String>();
						String regEx = "#\\{ *" + name_k + ".[a-z_A-Z]+\\d*\\ *\\}"; // 表示a或f
						Pattern p = Pattern.compile(regEx);
						Matcher m = p.matcher(sql);
						while (m.find())
						{
							String name_1 = m.group(0);
							String value_1 = "";
							String regEx_2 = "#\\{ *" + name_k + ".(.*?) *\\}"; // 表示a或f
							Pattern p_2 = Pattern.compile(regEx_2);
							Matcher m_2 = p_2.matcher(name_1);
							while (m_2.find())
							{
								value_1 = m_2.group(1);
							}
							names.put(name_1, value_1);
						}
						if (names.size() > 0)// 如果自定义的大于0
						{
							// 这里进行一系列的注入处理
							Field fields[] = value.getClass().getDeclaredFields();
							for (Map.Entry<String, String> entry : names.entrySet())
							{
								String v = entry.getValue();
								for (Field field : fields)
								{
									if (field.getName().equals(v))
									{
										Method vMethod = null;
										// 判断是什么类型
										if (field.getType().equals(boolean.class)
												|| field.getType().equals(Boolean.class))
										{
											try
											{
												String newStr = v.substring(0, 1).toUpperCase(Locale.getDefault())
														+ v.replaceFirst("\\w", "");
												vMethod = value.getClass().getDeclaredMethod("is" + newStr);
											} catch (NoSuchMethodException e)
											{
											}
										} else
										{
											try
											{
												String newStr = v.substring(0, 1).toUpperCase(Locale.getDefault())
														+ v.replaceFirst("\\w", "");
												vMethod = value.getClass().getDeclaredMethod("get" + newStr);
											} catch (NoSuchMethodException e)
											{
											}
										}
										if (vMethod != null)
										{
											try
											{
												sql = sql.replaceAll("#\\{ *" + name_k + "." + v + " *\\}",
														"'" + vMethod.invoke(value) + "'");
												// sql = sql.replaceAll("$\\{
												// *"+name_k+"."+v+"
												// *\\}","'"+vMethod.invoke(value)+"'");
											} catch (IllegalAccessException | IllegalArgumentException
													| InvocationTargetException e)
											{
												e.printStackTrace();
											}
										}

									}
								}
							}

						}

						// 直接调用toString方法取值
						// #{a.[a-zA-Z]+\d*} 匹配的正则表达式
						sql = sql.replaceAll("\\#\\{ *" + name_k + " *\\}", "'" + value + "'");
						// sql = sql.replaceAll("\\$\\{ *" + name_k + " *\\}",
						// "'" + value + "'");
					}
					continue;
				}
			}
		}
		System.out.println("NativeSql:\n" + sql);
		return sql;
	}
}
