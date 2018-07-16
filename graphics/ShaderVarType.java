package graphics;

import com.badlogic.gdx.graphics.GL20;

/**
 * Created by wuhao on 7/22/16.
 */
public enum ShaderVarType {
    C4B(GL20.GL_UNSIGNED_BYTE, 4, 4) // color 4 B
    , TEXTURE(0)
    , F1(1), VEC2(2), VEC3(3), VEC4(4), Mat2(2 * 2), Mat3(3 * 3), Mat4(4 * 4);

    ShaderVarType(int components) {
        this.type = GL20.GL_FLOAT;
        this.components = components;
        this.bytes = 4 * components;
    }

    ShaderVarType(int type, int components, int bytes) {
        this.type = type;
        this.components = components;
        this.bytes = bytes;
    }

    private int type;
    private int components;
    private int bytes;

    public int getGLType() {
        return this.type;
    }
    public int getNComponents() {
        return components;
    }
    public int getBytes() {
        return this.bytes;
    }
}
