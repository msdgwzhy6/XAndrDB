package com.xcode.xandrdb.Executer.Core;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.database.Cursor;

public class selectExecuterCore
{
	/**
	 * 将查询到的数据，转化成用户自定义的JavaBean <br>
	 * PS：这里还有一个问题，就是在参数为数组的时候，没有进行处理,但是我觉得没有必要，
	 * 因为sqlite数据在存进数据库的时候，都是以字符串的形式保存
	 * </br>
	 * 
	 * @param cursor
	 *            数据库的游标
	 * @param returnType
	 *            转化的JavaBean
	 * @return
	 */
	public static Object cusorToJavaBean(Cursor cursor, Class<?> returnType)
	{
		Object object = null;
		try
		{
			object = returnType.newInstance();
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		Method inMethods[] = returnType.getDeclaredMethods();
		if (cursor.moveToNext())
		{
			String colums[] = cursor.getColumnNames();
			for (Method m : inMethods)
			{
				String methenName = m.getName();
				if (methenName.startsWith("set"))// 先判断是不是set开头的
				{
					String queryName = m.getName().substring(3);// 查找出这个方法中，需要的参数名
					for (String colum : colums)
					{
						String low_colum = colum.toLowerCase(Locale.getDefault());// 将两个字符串全部转化成小写
						String low_queryname = queryName.toLowerCase(Locale.getDefault());// 将两个字符串全部转化成小写
						if (low_colum.equals(low_queryname))// 如果找到这个colum
						{
							String value = cursor.getString(cursor.getColumnIndex(colum));
							try
							{
								// 在执行反射之前，先判断一下输入的类型
								Class<?> paramtype = null;
								Class<?> paramtypes[] = m.getParameterTypes();
								if (paramtypes.length > 0)
								{
									paramtype = paramtypes[0];
								}
								// 如果类型是基本类型，或者是他们的包装类
								if (paramtype.isPrimitive() || paramtype.getName().startsWith("java.lang"))
								{
									// 才执行注入操作
									Object arg = null;
									if (paramtype.equals(int.class) || paramtype.equals(Integer.class))
									{
										try
										{
											arg = Integer.parseInt(value);
											m.invoke(object, arg);
										} catch (Exception e)
										{
										}
									} else if (paramtype.equals(byte.class) || paramtype.equals(Byte.class))
									{
										try
										{
											arg = Byte.parseByte(value);
											m.invoke(object, arg);
										} catch (Exception e)
										{
										}
									} else if (paramtype.equals(short.class) || paramtype.equals(Short.class))
									{
										try
										{
											arg = Short.parseShort(value);
											m.invoke(object, arg);
										} catch (Exception e)
										{
										}
									} else if (paramtype.equals(long.class) || paramtype.equals(Long.class))
									{
										try
										{
											arg = Long.parseLong(value);
											m.invoke(object, arg);
										} catch (Exception e)
										{
										}
									} else if (paramtype.equals(float.class) || paramtype.equals(Float.class))
									{
										try
										{
											arg = Float.parseFloat(value);
											m.invoke(object, arg);
										} catch (Exception e)
										{
										}
									} else if (paramtype.equals(double.class) || paramtype.equals(Double.class))
									{
										try
										{
											arg = Double.parseDouble(value);
											m.invoke(object, arg);
										} catch (Exception e)
										{
										}
									} else if (paramtype.equals(char.class) || paramtype.equals(Character.class))
									{
										try
										{
											arg = Character.valueOf(value.toCharArray()[0]);
											m.invoke(object, arg);
										} catch (Exception e)
										{
										}
									} else if (paramtype.equals(boolean.class) || paramtype.equals(Boolean.class))
									{
										try
										{
											arg = Boolean.parseBoolean(value);
											m.invoke(object, arg);
										} catch (Exception e)
										{
										}
									} else if (paramtype.equals(String.class))
									{
										try
										{
											arg = (value + "");
											m.invoke(object, arg);
										} catch (Exception e)
										{
										}
									}
								}
							} catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		return object;
	}

	/**
	 * 将查询到的数据，转化成Java简单的类型
	 * 
	 * @param cursor
	 *            游标
	 * @param clazz
	 *            简单类型
	 * @return
	 */
	public static Object cusorToSimpleClass(Cursor cursor, Class<?> clazz)
	{
		Object object = null;
		String columNames[] = cursor.getColumnNames();
		String value = null;
		if (cursor.moveToNext() && columNames.length > 0)
		{
			try
			{
				value = cursor.getString(0);
			} catch (Exception e)
			{
				value = "";
			}
		}
		try
		{
			if (clazz.equals(int.class) || clazz.equals(Integer.class))
			{
				object = Integer.parseInt(value);
			} else if (clazz.equals(byte.class) || clazz.equals(Byte.class))
			{
				object = Byte.parseByte(value);
			} else if (clazz.equals(short.class) || clazz.equals(Short.class))
			{
				object = Short.parseShort(value);
			} else if (clazz.equals(long.class) || clazz.equals(Long.class))
			{
				object = Long.parseLong(value);
			} else if (clazz.equals(float.class) || clazz.equals(Float.class))
			{
				object = Float.parseFloat(value);
			} else if (clazz.equals(double.class) || clazz.equals(Double.class))
			{
				object = Double.parseDouble(value);
			} else if (clazz.equals(char.class) || clazz.equals(Character.class))
			{
				object = Character.valueOf(value.toCharArray()[0]);
			} else if (clazz.equals(boolean.class) || clazz.equals(Boolean.class))
			{
				object = Boolean.parseBoolean(value);
			} else if (clazz.equals(String.class))
			{
				object = value;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

		return object;
	}

	/**
	 * 将从数据库中查取到的数据，转化成数组
	 * 
	 * @param cursor
	 *            游标
	 * @param returnType
	 *            返回类型
	 * @return 返回值
	 */
	public static Object cusorToArray(Cursor cursor, Class<?> returnType, Method method)
	{
		List<Object> list = new ArrayList<Object>();
		String columNames[] = cursor.getColumnNames();
		Class<?> returntype = null;
		try
		{
			if (returnType.equals(int[].class) || returnType.equals(Integer[].class))
			{
				while (cursor.moveToNext() && columNames.length > 0)
					list.add(Integer.parseInt(cursor.getString(0)));
			} else if (returnType.equals(byte[].class) || returnType.equals(Byte[].class))
			{
				while (cursor.moveToNext() && columNames.length > 0)
					list.add(Byte.parseByte(cursor.getString(0)));
			} else if (returnType.equals(short[].class) || returnType.equals(Short[].class))
			{
				while (cursor.moveToNext() && columNames.length > 0)
					list.add(Short.parseShort(cursor.getString(0)));
			} else if (returnType.equals(long[].class) || returnType.equals(Long[].class))
			{
				while (cursor.moveToNext() && columNames.length > 0)
					list.add(Long.parseLong(cursor.getString(0)));
			} else if (returnType.equals(float[].class) || returnType.equals(Float[].class))
			{
				while (cursor.moveToNext() && columNames.length > 0)
					list.add(Float.parseFloat(cursor.getString(0)));
			} else if (returnType.equals(double[].class) || returnType.equals(Double[].class))
			{
				while (cursor.moveToNext() && columNames.length > 0)
					list.add(Double.parseDouble(cursor.getString(0)));
			} else if (returnType.equals(char[].class) || returnType.equals(Character[].class))
			{
				while (cursor.moveToNext() && columNames.length > 0)
					list.add(Character.valueOf(cursor.getString(0).toCharArray()[0]));
			} else if (returnType.equals(boolean[].class) || returnType.equals(Boolean[].class))
			{
				while (cursor.moveToNext() && columNames.length > 0)
					list.add(Boolean.parseBoolean(cursor.getString(0)));
			} else if (returnType.equals(String[].class))
			{
				while (cursor.moveToNext() && columNames.length > 0)
					list.add(cursor.getString(0));
			} else // 如果不是八大基本类型，那么 就可能是自定义类型
			{
				returntype = returnType.getComponentType();
				Method inMethods[] = returntype.getDeclaredMethods();
				while (cursor.moveToNext() && columNames.length > 0)
				{
					Object object = returntype.newInstance();
					String colums[] = cursor.getColumnNames();
					for (Method m : inMethods)
					{
						String methenName = m.getName();
						if (methenName.startsWith("set"))// 先判断是不是set开头的
						{
							String queryName = m.getName().substring(3);// 查找出这个方法中，需要的参数名
							for (String colum : colums)
							{
								String low_colum = colum.toLowerCase(Locale.getDefault());// 将两个字符串全部转化成小写
								String low_queryname = queryName.toLowerCase(Locale.getDefault());// 将两个字符串全部转化成小写
								if (low_colum.equals(low_queryname))// 如果找到这个colum
								{
									String value = cursor.getString(cursor.getColumnIndex(colum));
									// 在执行反射之前，先判断一下输入的类型
									Class<?> paramtype = null;
									Class<?> paramtypes[] = m.getParameterTypes();
									if (paramtypes.length > 0)
									{
										paramtype = paramtypes[0];
									}
									// 如果类型是基本类型，或者是他们的包装类
									if (paramtype.isPrimitive() || paramtype.getName().startsWith("java.lang"))
									{
										// 才执行注入操作
										Object arg = null;
										if (paramtype.equals(int.class) || paramtype.equals(Integer.class))
										{
											arg = Integer.parseInt(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(byte.class) || paramtype.equals(Byte.class))
										{
											arg = Byte.parseByte(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(short.class) || paramtype.equals(Short.class))
										{
											arg = Short.parseShort(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(long.class) || paramtype.equals(Long.class))
										{
											arg = Long.parseLong(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(float.class) || paramtype.equals(Float.class))
										{
											arg = Float.parseFloat(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(double.class) || paramtype.equals(Double.class))
										{
											arg = Double.parseDouble(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(char.class) || paramtype.equals(Character.class))
										{
											arg = Character.valueOf(value.toCharArray()[0]);
											m.invoke(object, arg);
										} else if (paramtype.equals(boolean.class) || paramtype.equals(Boolean.class))
										{
											arg = Boolean.parseBoolean(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(String.class))
										{
											arg = (value + "");
											m.invoke(object, arg);
										}
									}
								}
							}
						}
					}
					list.add(object);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		if (returnType.equals(int[].class) || returnType.equals(Integer[].class))
		{
			Integer[] integers = list.toArray(new Integer[list.size()]);
			if (returnType.equals(int[].class))
			{
				int[] intArray = new int[integers.length];
				for (int i = 0; i < integers.length; i++)
				{
					intArray[i] = integers[i].intValue();
				}
				return intArray;
			}
			return integers;
		} else if (returnType.equals(byte[].class) || returnType.equals(Byte[].class))
		{
			Byte[] Bytes = list.toArray(new Byte[list.size()]);
			if (returnType.equals(byte[].class))
			{
				byte[] byteArray = new byte[Bytes.length];
				for (int i = 0; i < Bytes.length; i++)
				{
					byteArray[i] = Bytes[i].byteValue();
				}
				return byteArray;
			}
			return Bytes;

		} else if (returnType.equals(short[].class) || returnType.equals(Short[].class))
		{
			Short[] Shorts = list.toArray(new Short[list.size()]);
			if (returnType.equals(short[].class))
			{
				short[] shortArray = new short[Shorts.length];
				for (int i = 0; i < Shorts.length; i++)
				{
					shortArray[i] = Shorts[i].shortValue();
				}
				return shortArray;
			}
			return Shorts;
		} else if (returnType.equals(long[].class) || returnType.equals(Long[].class))
		{
			Long[] longs = list.toArray(new Long[list.size()]);
			if (returnType.equals(long[].class))
			{
				long[] longArray = new long[longs.length];
				for (int i = 0; i < longs.length; i++)
				{
					longArray[i] = longs[i].longValue();
				}
				return longArray;
			}
			return longs;
		} else if (returnType.equals(float[].class) || returnType.equals(Float[].class))
		{
			Float[] floats = list.toArray(new Float[list.size()]);
			if (returnType.equals(float[].class))
			{
				float[] floatArray = new float[floats.length];
				for (int i = 0; i < floats.length; i++)
				{
					floatArray[i] = floats[i].floatValue();
				}
				return floatArray;
			}
			return floats;
		} else if (returnType.equals(double[].class) || returnType.equals(Double[].class))
		{
			Double[] doubles = list.toArray(new Double[list.size()]);
			if (returnType.equals(double[].class))
			{
				double[] doubleArray = new double[doubles.length];
				for (int i = 0; i < doubles.length; i++)
				{
					doubleArray[i] = doubles[i].doubleValue();
				}
				return doubleArray;
			}
			return doubles;
		} else if (returnType.equals(char[].class) || returnType.equals(Character[].class))
		{
			Character[] characters = list.toArray(new Character[list.size()]);
			if (returnType.equals(char[].class))
			{
				char[] charArray = new char[characters.length];
				for (int i = 0; i < characters.length; i++)
				{
					charArray[i] = characters[i].charValue();
				}
				return charArray;
			}
			return characters;
		} else if (returnType.equals(boolean[].class) || returnType.equals(Boolean[].class))
		{
			Boolean[] booleans = list.toArray(new Boolean[list.size()]);
			if (returnType.equals(boolean[].class))
			{
				boolean[] boolArray = new boolean[booleans.length];
				for (int i = 0; i < booleans.length; i++)
				{
					boolArray[i] = booleans[i].booleanValue();
				}
				return boolArray;
			}
			return booleans;
		} else if (returnType.equals(String[].class))
		{
			return list.toArray(new String[list.size()]);
		} else//自定義bean
		{
			Object newArray = Array.newInstance(returntype, list.size()); //重中之重，通过类型而实例化一个固定大小的数组 
			System.arraycopy(list.toArray(), 0, newArray, 0, list.size());
			list.clear();
			list = null;
			return newArray;
		}
	}

	/**
	 * 将从数据库中查取到的数据，转化成List
	 * 
	 * @param cursor
	 *            游标
	 * @param returnType
	 *            返回类型
	 * @return 返回值
	 */
	@SuppressWarnings("rawtypes")
	public static Object cusorToList(Cursor cursor, Class<?> returnType, Method method)
	{
		
		List<Object> list = new ArrayList<>();
		Type returnType2 = method.getGenericReturnType();
		Class typeArgClass= Object.class;
		if(returnType2 instanceof ParameterizedType)
		{
		    ParameterizedType type = (ParameterizedType) returnType2;
		    Type[] typeArguments = type.getActualTypeArguments();//获取list里面写的泛型是什么
		    for(Type typeArgument : typeArguments)
		    {
		        typeArgClass = (Class) typeArgument;
		        //System.out.println("typeArgClass = " + typeArgClass);
		    }
		}
		
		String columNames[] = cursor.getColumnNames();
		while (cursor.moveToNext() && columNames.length > 0)
		{
			try
			{
				if (typeArgClass.equals(int.class) || typeArgClass.equals(Integer.class))
				{
					list.add(Integer.parseInt(cursor.getString(0)));
				} else if (typeArgClass.equals(byte.class) || typeArgClass.equals(Byte.class))
				{
					list.add(Byte.parseByte(cursor.getString(0)));
				} else if (typeArgClass.equals(short.class) || typeArgClass.equals(Short.class))
				{
					list.add(Short.parseShort(cursor.getString(0)));
				} else if (typeArgClass.equals(long.class) || typeArgClass.equals(Long.class))
				{
					list.add(Long.parseLong(cursor.getString(0)));
				} else if (typeArgClass.equals(float.class) || typeArgClass.equals(Float.class))
				{
					list.add(Float.parseFloat(cursor.getString(0)));
				} else if (typeArgClass.equals(double.class) || typeArgClass.equals(Double.class))
				{
					list.add(Double.parseDouble(cursor.getString(0)));
				} else if (typeArgClass.equals(char.class) || typeArgClass.equals(Character.class))
				{
					list.add(Character.valueOf(cursor.getString(0).toCharArray()[0]));
				} else if (typeArgClass.equals(boolean.class) || typeArgClass.equals(Boolean.class))
				{
					list.add(Boolean.parseBoolean(cursor.getString(0)));
				} else if (typeArgClass.equals(String.class))
				{
					list.add(cursor.getString(0));
				} else // 如果不是八大基本类型，那么 就可能是自定义类型
				{
					Method inMethods[] = typeArgClass.getDeclaredMethods();
					String colums[] = cursor.getColumnNames();
					for (Method m : inMethods)
					{
						String methenName = m.getName();
						if (methenName.startsWith("set"))// 先判断是不是set开头的
						{
							String queryName = m.getName().substring(3);// 查找出这个方法中，需要的参数名
							for (String colum : colums)
							{
								String low_colum = colum.toLowerCase(Locale.getDefault());// 将两个字符串全部转化成小写
								String low_queryname = queryName.toLowerCase(Locale.getDefault());// 将两个字符串全部转化成小写
								if (low_colum.equals(low_queryname))// 如果找到这个colum
								{
									Object object = typeArgClass.newInstance();
									String value = cursor.getString(cursor.getColumnIndex(colum));
									// 在执行反射之前，先判断一下输入的类型
									Class<?> paramtype = null;
									Class<?> paramtypes[] = m.getParameterTypes();
									if (paramtypes.length > 0)
									{
										paramtype = paramtypes[0];
									}
									// 如果类型是基本类型，或者是他们的包装类
									if (paramtype.isPrimitive() || paramtype.getName().startsWith("java.lang"))
									{
										// 才执行注入操作
										Object arg = null;
										if (paramtype.equals(int.class) || paramtype.equals(Integer.class))
										{
											arg = Integer.parseInt(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(byte.class) || paramtype.equals(Byte.class))
										{
											arg = Byte.parseByte(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(short.class) || paramtype.equals(Short.class))
										{
											arg = Short.parseShort(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(long.class) || paramtype.equals(Long.class))
										{
											arg = Long.parseLong(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(float.class) || paramtype.equals(Float.class))
										{
											arg = Float.parseFloat(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(double.class) || paramtype.equals(Double.class))
										{
											arg = Double.parseDouble(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(char.class) || paramtype.equals(Character.class))
										{
											arg = Character.valueOf(value.toCharArray()[0]);
											m.invoke(object, arg);
										} else if (paramtype.equals(boolean.class) || paramtype.equals(Boolean.class))
										{
											arg = Boolean.parseBoolean(value);
											m.invoke(object, arg);
										} else if (paramtype.equals(String.class))
										{
											arg = (value + "");
											m.invoke(object, arg);
										}
									}
									list.add(object);
								}
							}
						}
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		return list;
	}
}
