package net.nonemc.leaf.script.remapper.injection.transformers.handlers

import net.nonemc.leaf.script.ScriptSafetyManager
import net.nonemc.leaf.script.remapper.Remapper
import org.objectweb.asm.Type
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Method

/**
 * ...
 *
 * @author CCBlueX
 */
object AbstractJavaLinkerHandler {

    /**
     * Handle member set name to hashmap of AbstractJavaLinkerHandler
     *
     * Name will be remapped from srgs
     * Example: swingItem to func_71038_i
     *
     * @class jdk/internal/dynalink/beans/AbstractJavaLinker
     * @method addMember(Ljava/lang/String;Ljava/lang/reflect/AccessibleObject;Ljava/util/Map;)V
     * @param name of member set
     * @param accessibleObject method of member set
     */
    @JvmStatic
    fun addMember(clazz: Class<*>, name: String, accessibleObject: AccessibleObject): String {
        if (accessibleObject !is Method) {
            return name
        }

        var currentClass = clazz
        while (currentClass.name != "java.lang.Object") {
            if (ScriptSafetyManager.isRestrictedSimple(currentClass, name)) {
                return "RESTRICTED"
            }
            val remapped = Remapper.remapMethod(currentClass, name, Type.getMethodDescriptor(accessibleObject))

            if (remapped != name) {
                return remapped
            }

            if (currentClass.superclass == null) {
                break
            }

            currentClass = currentClass.superclass
        }

        return name
    }

    /**
     * Handle member set name to hashmap of AbstractJavaLinkerHandler
     *
     * Name will be remapped from srgs
     * Example: thePlayer to field_71439_g
     *
     * @class jdk/internal/dynalink/beans/AbstractJavaLinker
     * @method addMember(Ljava/lang/String;Ljdk/internal/dynalink/beans/SingleDynamicMethod;Ljava/util/Map;)V
     * @param name of property getter
     */
    @JvmStatic
    fun addMember(clazz: Class<*>, name: String): String {
        var currentClass = clazz
        while (currentClass.name != "java.lang.Object") {
            if (ScriptSafetyManager.isRestrictedSimple(currentClass, name)) {
                return "RESTRICTED"
            }
            val remapped = Remapper.remapField(currentClass, name)

            if (remapped != name) {
                return remapped
            }

            if (currentClass.superclass == null) {
                break
            }

            currentClass = currentClass.superclass
        }

        return name
    }

    /**
     * Handle property getter set name to hashmap of AbstractJavaLinkerHandler
     *
     * Name will be remapped from srgs
     * Example: thePlayer to field_71439_g
     *
     * @class jdk/internal/dynalink/beans/AbstractJavaLinker
     * @method setPropertyGetter(Ljava/lang/String;Ljdk/internal/dynalink/beans/SingleDynamicMethod;Ljdk/internal/dynalink/beans/GuardedInvocationComponent$ValidationType;)V
     * @param name of property getter
     */
    @JvmStatic
    fun setPropertyGetter(clazz: Class<*>, name: String): String {
        var currentClass = clazz
        while (currentClass.name != "java.lang.Object") {
            if (ScriptSafetyManager.isRestrictedSimple(currentClass, name)) {
                return "RESTRICTED"
            }
            val remapped = Remapper.remapField(currentClass, name)

            if (remapped != name) {
                return remapped
            }

            if (currentClass.superclass == null) {
                break
            }

            currentClass = currentClass.superclass
        }

        return name
    }
}