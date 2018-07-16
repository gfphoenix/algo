package graphics;

/**
 * Created by wuhao on 7/22/16.
 */
public class ShaderAttribs {
    public static class ShaderAttrib extends Shader.ShaderVar {
        int offset = -1;
        boolean normalized;
        public ShaderAttrib(ShaderVarType type, String name) {
            super(type, name);
            setNormalized();
        }
        private void setNormalized() {
            setNormalized(type()==ShaderVarType.C4B);
        }
        public void setNormalized(boolean normalized) {
            this.normalized = normalized;
        }
        public boolean isNormalized() {
            return normalized;
        }
        public int offset() {
            return offset;
        }
        @Override
        public String toString() {
            return super.toString() + " offset=" + offset + "  normalized=" + normalized;
        }
    }
    ShaderAttrib []attribs;
    private int size;// one vertices size in byte

    public ShaderAttribs(ShaderAttrib... attribs) {
        this.attribs = attribs;
        updateOffset();
    }

    private void updateOffset() {
        int n = attribs.length;
        attribs[0].offset = 0;
        for (int i = 1; i < n; i++) {
            attribs[i].offset = attribs[i-1].offset + attribs[i-1].type().getBytes();
        }
        size = attribs[n-1].offset + attribs[n-1].type().getBytes();
    }
    public int getVertexSize() {
        return size;
    }
    public int getAttribNumber() {
        return attribs.length;
    }

    public ShaderAttrib attribAt(int i) {
        return attribs[i];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("attribs len = ").append(attribs.length)
                .append(" vertices size=").append(size).append('\n');
        for (ShaderAttrib attrib : attribs)
            sb.append("[ ").append(attrib).append(" ]\n");
        return sb.toString();
    }
}
