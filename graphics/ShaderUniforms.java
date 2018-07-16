package graphics;

/**
 * Created by wuhao on 7/22/16.
 */
public class ShaderUniforms {
        private Shader.ShaderVar[] uniforms;

        public ShaderUniforms(Shader.ShaderVar... uniforms) {
            this.uniforms = uniforms;
        }

        public int getUniformNumber() {
            return uniforms.length;
        }

        public Shader.ShaderVar uniformAt(int i) {
            return uniforms[i];
        }

        public void setUniformValues(Shader shader){
        }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("uniforms len=").append(uniforms.length).append('\n');
        for (int i = 0; i < uniforms.length; i++) {
            sb.append("[ ").append(uniforms[i]).append(" ]\n");
        }
        return sb.toString();
    }
}
