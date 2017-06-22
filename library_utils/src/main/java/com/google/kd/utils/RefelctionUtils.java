package com.google.kd.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public abstract class RefelctionUtils {
	private static final String TAG = RefelctionUtils.class.getSimpleName();

	public static Class<?> getClass(String paramString) {
		try {
			Class localClass = Class.forName(paramString);
			return localClass;
		} catch (ClassNotFoundException localClassNotFoundException) {
			Log.w(TAG, "className not found:" + paramString);
		}
		return null;
	}

	public static Object getInstance(String paramString) {
		try {
			Object localObject = Class.forName(paramString).getConstructor(new Class[0]).newInstance(new Object[0]);
			return localObject;
		} catch (ClassNotFoundException localClassNotFoundException) {
			Log.e(TAG, "Occur an ClassNotFoundException when construct");
			return null;
		} catch (Exception localException) {
			for (;;) {
				Log.e(TAG, "Occur an exception when construct");
			}
		}
	}

	public static Method getMethod(Class<?> paramClass, String paramString, Class<?>... paramVarArgs) {
		if ((paramClass == null) || (paramString == null)) {
			return null;
		}
		try {
			Method localMethod = paramClass.getMethod(paramString, paramVarArgs);
			return localMethod;
		} catch (SecurityException localSecurityException) {
			Log.w(TAG, localSecurityException.getCause());
			return null;
		} catch (NoSuchMethodException localNoSuchMethodException) {
			Log.w(TAG, paramString + ", not such method.");
		}
		return null;
	}

	public static Method getMethod(String paramString, Class<?> paramClass, Object[] paramArrayOfObject) throws Exception {
		for (Method localMethod : paramClass.getMethods()) {
			if (localMethod.getName().equals(paramString)) {
				Class[] arrayOfClass = localMethod.getParameterTypes();
				if (paramArrayOfObject == null) {
					if ((arrayOfClass != null) && (arrayOfClass.length != 0)) {
					}
				} else {
					while (paramArrayOfObject.length == arrayOfClass.length) {
						return localMethod;
					}
				}
			}
		}
		throw new Exception();
	}

	public static Method getMethod(String paramString, Object paramObject, Object[] paramArrayOfObject) throws Exception {
		return getMethod(paramString, paramObject.getClass(), paramArrayOfObject);
	}

	public static Object getStaticVariableValue(Class<?> paramClass, String paramString) throws Exception {
		try {
			Object localObject = paramClass.getField(paramString).get(null);
			return localObject;
		} catch (NoSuchFieldException localNoSuchFieldException) {
			throw new Exception(localNoSuchFieldException);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw new Exception(localIllegalArgumentException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new Exception(localIllegalAccessException);
		}
	}

	public static Object getStaticVariableValue(String paramString1, String paramString2) throws Exception {
		try {
			Object localObject = getStaticVariableValue(Class.forName(paramString1), paramString2);
			return localObject;
		} catch (Exception localException) {
			throw new Exception(localException);
		}
	}

	public static Object invoke(Object paramObject, Method paramMethod, Object... paramVarArgs) throws Exception {
		if (paramMethod == null) {
			throw new Exception();
		}
		try {
			Object localObject = paramMethod.invoke(paramObject, paramVarArgs);
			return localObject;
		} catch (RuntimeException localRuntimeException) {
			Log.e(TAG, "Exception in invoke: " + localRuntimeException.getClass().getSimpleName());
			if ("com.huawei.android.util.NoExtAPIException".equals(localRuntimeException.getClass().getName())) {
				throw new Exception();
			}
		} catch (Exception localException) {
			Log.e(TAG, "Exception in invoke: " + localException.getCause() + "; method=" + paramMethod.getName());
			throw new Exception();
		}
		return paramVarArgs;
	}

	public static Object invokeInnerClass(String paramString1, String paramString2, String paramString3, Object[] paramArrayOfObject) throws Exception {
		try {
			Class[] arrayOfClass = Class.forName(paramString1).getClasses();
			if (arrayOfClass != null) {
				int i = arrayOfClass.length;
				for (int j = 0; j < i; j++) {
					Class localClass = arrayOfClass[j];
					if (localClass.getSimpleName().equals(paramString2)) {
						Object localObject = invokeMethod(paramString3, localClass, paramArrayOfObject);
						return localObject;
					}
				}
			}
			return null;
		} catch (Exception localException) {
			throw new Exception(localException);
		}
	}

	public static Object invokeMethod(String paramString, Class<?> paramClass, Object[] paramArrayOfObject) throws Exception {
		Method localMethod = getMethod(paramString, paramClass, paramArrayOfObject);
		try {
			Object localObject = localMethod.invoke(null, paramArrayOfObject);
			return localObject;
		} catch (Exception localException) {
			throw new Exception(localException);
		}
	}

	public static Object invokeMethod(String paramString, Object paramObject, Object[] paramArrayOfObject) throws Exception {
		Method localMethod = getMethod(paramString, paramObject, paramArrayOfObject);
		try {
			Object localObject = localMethod.invoke(paramObject, paramArrayOfObject);
			return localObject;
		} catch (Exception localException) {
			localException.printStackTrace();
			throw new Exception(localException);
		}
	}

	public static Object invokeMethod(String paramString1, String paramString2, Object[] paramArrayOfObject) throws Exception {
		try {
			Object localObject = getMethod(paramString1, Class.forName(paramString2), paramArrayOfObject).invoke(null, paramArrayOfObject);
			return localObject;
		} catch (Exception localException) {
			throw new Exception(localException);
		}
	}

	@SuppressLint("NewApi")
	public static Object invokeMethodFromDex(String paramString1, Context paramContext, String paramString2, String paramString3, Class<?>[] paramArrayOfClass, Object paramObject, Object[] paramArrayOfObject) {
		DexClassLoader localDexClassLoader = loadDex(paramString1, paramContext);
		if (localDexClassLoader == null) {
			return null;
		}
		try {
			Method localMethod = localDexClassLoader.loadClass(paramString2).getMethod(paramString3, paramArrayOfClass);
			localMethod.setAccessible(true);
			Object localObject = localMethod.invoke(paramObject, paramArrayOfObject);
			return localObject;
		} catch (Exception localException) {
			String str = TAG;
			StringBuilder localStringBuilder = new StringBuilder().append("invokeMethodFromDex dexPath(");
			if (paramString1 == null) {
				paramString1 = "null";
			}
			Log.i(str, paramString1 + ") class(" + paramString2 + ") " + " method(" + paramString3 + ") " + localException);
		}
		return null;
	}

	public static boolean isLibraryInstalled(Context paramContext, String paramString) {
		if ((paramString == null) || (paramContext == null)) {
		}
		for (;;) {
			return false;
			// try {
			// String[] arrayOfString = paramContext.getPackageManager()
			// .getSystemSharedLibraryNames();
			// int i = arrayOfString.length;
			// for (int j = 0; j < i; j++) {
			// boolean bool = paramString.equals(arrayOfString[j]);
			// if (bool) {
			// return true;
			// }
			// }
			// return false;
			// }
			// catch (Exception localException) {
			// Log.e(TAG, "isLibraryInstalled libName(" + paramString + ") "
			// + localException);
			// }
		}
	}

	public static boolean isSameTypeOrParent(Class<?> paramClass1, Class<?> paramClass2) {
		if (paramClass1 == paramClass2) {
		}
		while ((Object.class.equals(paramClass1)) || (paramClass1 == paramClass2.getSuperclass())) {
			return true;
		}
		return false;
	}

	private static DexClassLoader loadDex(String paramString, Context paramContext) {
		if (paramContext == null) {
			return null;
		}
		if (paramString == null) {
			paramString = "/system/framework/com.android.contacts.separated.jar";
		}
		return new DexClassLoader(paramString, paramContext.getDir("dex", 0).getAbsolutePath(), null, paramContext.getClassLoader());
	}
}
