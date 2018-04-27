package com.hahuge.myweb.commom.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BeanUtils {

    private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> toMap(T bean) {
        Map<String, Object> map;
        try {
            map = PropertyUtils.describe(bean);
            map.remove("class");
            return map;
        } catch (Exception e) {
            logger.error(String.format("bean to map due to fail. bean=[%s]", bean.toString()), e);
            return null;
        }

    }


//    private static Map describe(Object bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//        if(bean == null) {
//            throw new IllegalArgumentException("No bean specified");
//        } else {
//            HashMap description = new HashMap();
//            int i;
//            String name;
//            if(bean instanceof DynaBean) {
//                DynaProperty[] descriptors = ((DynaBean)bean).getDynaClass().getDynaProperties();
//
//                for(i = 0; i < descriptors.length; ++i) {
//                    name = descriptors[i].getName();
//                    description.put(name, PropertyUtils.getProperty(bean, name));
//                }
//            } else {
//                PropertyDescriptor[] var5 = PropertyUtils.getPropertyDescriptors(bean);
//
//                for(i = 0; i < var5.length; ++i) {
//                    Method m = var5[i].getReadMethod();
//                    if(m==null){
//                        continue;
//                    }
//                    //Transient 注解将被忽略
//                    Transient aTransient = m.getAnnotation(Transient.class);
//                    if(null!=aTransient){
//                        continue;
//                    }
//                    if(m != null ) {
//                        name = var5[i].getName();
//                        description.put(name, PropertyUtils.getProperty(bean, name));
//                    }
//                }
//            }
//
//            return description;
//        }
//    }
}
