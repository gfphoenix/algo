package graphics;

import java.nio.FloatBuffer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by wuhao on 7/21/16.
 */
public class SpriteShader extends ShaderHelper {
    static final int VERTEX_SIZE = 5;
    Texture lastTexture = null;
    float invTexWidth = 0, invTexHeight = 0;

    // make sure you know how to determine the buffer size
    public SpriteShader(String vert_source, String frag_source, ShaderAttribs attribs, ShaderUniforms uniforms, Mesh mesh, int floats, int shorts) {
        init(vert_source, frag_source, attribs, uniforms, mesh, floats, shorts);
    }

    // set buffer size based on the size of mesh
    public SpriteShader(String vert_source, String frag_source, ShaderAttribs attribs, ShaderUniforms uniforms, Mesh mesh) {
        init(vert_source, frag_source, attribs, uniforms, mesh);
    }

    public SpriteShader(String vert_source, String frag_source, ShaderAttribs attribs, ShaderUniforms uniforms) {
        init(vert_source, frag_source, attribs, uniforms);
    }
    private void init(Mesh mesh, int floats, int shorts) {
        String vertexShader = "attribute vec2 a_pos;\n" //
                + "attribute vec4 a_color;\n" //
                + "attribute vec2 a_tex0;\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = a_color;\n" //
                + "   v_texCoords = a_tex0;\n" //
                + "   gl_Position =  u_projTrans * vec4(a_pos,0.0,1.0);\n" //
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying LOWP vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
//                + "  gl_FragColor = v_color;\n"
                + "}";

        final ShaderAttribs attribs = new ShaderAttribs(
                new ShaderAttribs.ShaderAttrib(ShaderVarType.VEC2, "a_pos")
                , new ShaderAttribs.ShaderAttrib(ShaderVarType.C4B, "a_color")
                , new ShaderAttribs.ShaderAttrib(ShaderVarType.VEC2, "a_tex0")
        );
        final ShaderUniforms uniforms = new ShaderUniforms(
                new ShaderVar(ShaderVarType.Mat4, "u_projTrans"),
                new ShaderVar(ShaderVarType.TEXTURE, "u_texture")) {
            public void setUniformValues(Shader shader) {
                setUniformValues__(renderer.getFloatBuffer());
            }
        };
        init(vertexShader, fragmentShader, attribs, uniforms, mesh, floats, shorts);

        System.out.println("float cap = " + mesh.getVertexBufferSizeInFloats());
        System.out.println("short cap = " + mesh.getIndexBufferSizeInShorts());
    }
    private void init(Mesh mesh) {
        init(mesh, getFloatsByMesh(mesh), getShortsByMesh(mesh));
    }
    public SpriteShader() {
        init(createDefaultMesh());
    }
    public SpriteShader(Mesh mesh) {
        init(mesh);
    }
    public SpriteShader(Mesh mesh, int floats, int shorts) {
        init(mesh, floats, shorts);
    }

    @Override
    protected Mesh createDefaultMesh() {
        Mesh mesh = new Mesh();
        final int floats = 1000;
        final int shorts = 1000;
        boolean ok = mesh.initBuffers(floats, shorts);
        System.out.println("sprite shader mesh init = " + ok);
        return mesh;
    }

    private void setUniformValues__(FloatBuffer buffer) {
        SpriteShader ss = this;
        Texture tex = ss.getLastTexture();
        if (tex == null) return;
        L.i("start set uniform values");
        ShaderUniforms us = ss.getUniforms();
        // 0 : matrix
        ShaderVar sv = us.uniformAt(0);
        OrthographicCamera camera = renderer.getCamera();
        camera.update();
        buffer.clear();
        buffer.put(camera.combined.val);
        buffer.flip();
        gl.glUniformMatrix4fv(sv.location(), 1, false, buffer);
        L.i("set M4");
        // 1 : texture sampler
        sv = us.uniformAt(1);
        // no such this cap
        // gl.glEnable(GL20.GL_TEXTURE_2D);
        L.i("enable texture 2d");
        gl.glActiveTexture(GL20.GL_TEXTURE0);
        L.i("active texture");
        tex.bind();
        L.i("bind texture");
        gl.glUniform1i(sv.location(), 0);
        gl.glEnable(GL20.GL_BLEND);
        gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        L.i("set blend");
//        gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
//        gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);
//        gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE);
//        gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE);
        L.i("set texture parameter");
    }

    public Texture getLastTexture() {
        return lastTexture;
    }

    void switchTexture(Texture texture) {
        flush();
        lastTexture = texture;
        if (texture != null) {
            invTexWidth = 1f / texture.getWidth();
            invTexHeight = 1f / texture.getHeight();
        }
    }

    public void flush() {
        int count = vertexIndex;
        if (lastTexture == null || count == 0) return;

        mesh.useShader(this);
        mesh.setDrawMode(GL20.GL_TRIANGLES);
//        mesh.useShader(this);
        mesh.setVertices(vertices, 0, count);
        mesh.setIndices(indices, 0, shortIndex);
        mesh.flush();
        vertexIndex = 0;
        shortIndex = 0;
    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The rectangle is offset by
     * originX, originY relative to the origin. Scale specifies the scaling factor by which the rectangle should be scaled around
     * originX, originY. Rotation specifies the angle of counter clockwise rotation of the rectangle around originX, originY. The
     * portion of the {@link Texture} given by srcX, srcY and srcWidth, srcHeight is used. These coordinates and sizes are given in
     * texels. FlipX and flipY specify whether the texture portion should be fliped horizontally or vertically.
     *
     * @param x         the x-coordinate in screen space
     * @param y         the y-coordinate in screen space
     * @param originX   the x-coordinate of the scaling and rotation origin relative to the screen space coordinates
     * @param originY   the y-coordinate of the scaling and rotation origin relative to the screen space coordinates
     * @param width     the width in pixels
     * @param height    the height in pixels
     * @param scaleX    the scale of the rectangle around originX/originY in x
     * @param scaleY    the scale of the rectangle around originX/originY in y
     * @param rotation  the angle of counter clockwise rotation of the rectangle around originX/originY
     * @param srcX      the x-coordinate in texel space
     * @param srcY      the y-coordinate in texel space
     * @param srcWidth  the source with in texels
     * @param srcHeight the source height in texels
     * @param flipX     whether to flip the sprite horizontally
     * @param flipY     whether to flip the sprite vertically
     */
    public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX,
                     float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
        float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (vertices.length - vertexIndex < 4 * VERTEX_SIZE || indices.length - shortIndex < 6) //
            flush();

        // bottom left and top right corner points relative to origin
        final float worldOriginX = x + originX;
        final float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;

        // scale
        if (scaleX != 1 || scaleY != 1) {
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }

        // construct corner points, start from top left and go counter clockwise
        final float p1x = fx;
        final float p1y = fy;
        final float p2x = fx;
        final float p2y = fy2;
        final float p3x = fx2;
        final float p3y = fy2;
        final float p4x = fx2;
        final float p4y = fy;

        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float x4;
        float y4;

        // rotate
        if (rotation != 0) {
            final float cos = MathUtils.cosDeg(rotation);
            final float sin = MathUtils.sinDeg(rotation);

            x1 = cos * p1x - sin * p1y;
            y1 = sin * p1x + cos * p1y;

            x2 = cos * p2x - sin * p2y;
            y2 = sin * p2x + cos * p2y;

            x3 = cos * p3x - sin * p3y;
            y3 = sin * p3x + cos * p3y;

            x4 = x1 + (x3 - x2);
            y4 = y3 - (y2 - y1);
        } else {
            x1 = p1x;
            y1 = p1y;

            x2 = p2x;
            y2 = p2y;

            x3 = p3x;
            y3 = p3y;

            x4 = p4x;
            y4 = p4y;
        }

        x1 += worldOriginX;
        y1 += worldOriginY;
        x2 += worldOriginX;
        y2 += worldOriginY;
        x3 += worldOriginX;
        y3 += worldOriginY;
        x4 += worldOriginX;
        y4 += worldOriginY;

        float u = srcX * invTexWidth;
        float v = (srcY + srcHeight) * invTexHeight;
        float u2 = (srcX + srcWidth) * invTexWidth;
        float v2 = srcY * invTexHeight;

        if (flipX) {
            float tmp = u;
            u = u2;
            u2 = tmp;
        }

        if (flipY) {
            float tmp = v;
            v = v2;
            v2 = tmp;
        }

        float color = this.colorBits;
        int idx = this.vertexIndex / VERTEX_SIZE;
        int shortIdx = this.shortIndex;
        indices[shortIdx++] = (short) (idx);
        indices[shortIdx++] = (short) (idx + 1);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 3);
        indices[shortIdx++] = (short) (idx);
        this.shortIndex = shortIdx;
        idx = this.vertexIndex;

        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = x3;
        vertices[idx++] = y3;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = x4;
        vertices[idx++] = y4;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;
        this.vertexIndex = idx;
    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The portion of the
     * {@link Texture} given by srcX, srcY and srcWidth, srcHeight is used. These coordinates and sizes are given in texels. FlipX
     * and flipY specify whether the texture portion should be fliped horizontally or vertically.
     *
     * @param x         the x-coordinate in screen space
     * @param y         the y-coordinate in screen space
     * @param width     the width in pixels
     * @param height    the height in pixels
     * @param srcX      the x-coordinate in texel space
     * @param srcY      the y-coordinate in texel space
     * @param srcWidth  the source with in texels
     * @param srcHeight the source height in texels
     * @param flipX     whether to flip the sprite horizontally
     * @param flipY     whether to flip the sprite vertically
     */
    public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth,
                     int srcHeight, boolean flipX, boolean flipY) {
        float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (vertices.length - vertexIndex < 4 * VERTEX_SIZE || indices.length - shortIndex < 6) //
            flush();

        float u = srcX * invTexWidth;
        float v = (srcY + srcHeight) * invTexHeight;
        float u2 = (srcX + srcWidth) * invTexWidth;
        float v2 = srcY * invTexHeight;
        final float fx2 = x + width;
        final float fy2 = y + height;

        if (flipX) {
            float tmp = u;
            u = u2;
            u2 = tmp;
        }

        if (flipY) {
            float tmp = v;
            v = v2;
            v2 = tmp;
        }

        float color = this.colorBits;
        int idx = this.vertexIndex / VERTEX_SIZE;
        int shortIdx = this.shortIndex;
        indices[shortIdx++] = (short) (idx);
        indices[shortIdx++] = (short) (idx + 1);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 3);
        indices[shortIdx++] = (short) (idx);
        this.shortIndex = shortIdx;
        idx = this.vertexIndex;
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;
        this.vertexIndex = idx;
    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The portion of the
     * {@link Texture} given by srcX, srcY and srcWidth, srcHeight are used. These coordinates and sizes are given in texels.
     *
     * @param x         the x-coordinate in screen space
     * @param y         the y-coordinate in screen space
     * @param srcX      the x-coordinate in texel space
     * @param srcY      the y-coordinate in texel space
     * @param srcWidth  the source with in texels
     * @param srcHeight the source height in texels
     */
    public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
        float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (vertices.length - vertexIndex < 4 * VERTEX_SIZE || indices.length - shortIndex < 6) //
            flush();

        final float u = srcX * invTexWidth;
        final float v = (srcY + srcHeight) * invTexHeight;
        final float u2 = (srcX + srcWidth) * invTexWidth;
        final float v2 = srcY * invTexHeight;
        final float fx2 = x + srcWidth;
        final float fy2 = y + srcHeight;

        float color = this.colorBits;
        int idx = this.vertexIndex / VERTEX_SIZE;
        int shortIdx = this.shortIndex;
        indices[shortIdx++] = (short) (idx);
        indices[shortIdx++] = (short) (idx + 1);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 3);
        indices[shortIdx++] = (short) (idx);
        this.shortIndex = shortIdx;
        idx = this.vertexIndex;
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;
        this.vertexIndex = idx;
    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The portion of the
     * {@link Texture} given by u, v and u2, v2 are used. These coordinates and sizes are given in texture size percentage. The
     * rectangle will have the given tint {@link Color}.
     *
     * @param x      the x-coordinate in screen space
     * @param y      the y-coordinate in screen space
     * @param width  the width in pixels
     * @param height the height in pixels
     */
    public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
        float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (vertices.length - vertexIndex < 4 * VERTEX_SIZE || indices.length - shortIndex < 6) //
            flush();

        final float fx2 = x + width;
        final float fy2 = y + height;

        float color = this.colorBits;
        int idx = this.vertexIndex / VERTEX_SIZE;
        int shortIdx = this.shortIndex;
        indices[shortIdx++] = (short) (idx);
        indices[shortIdx++] = (short) (idx + 1);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 3);
        indices[shortIdx++] = (short) (idx);
        this.shortIndex = shortIdx;
        idx = this.vertexIndex;
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;
        this.vertexIndex = idx;
    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the width and height of the texture.
     *
     * @param x the x-coordinate in screen space
     * @param y the y-coordinate in screen space
     */
    public void draw(Texture texture, float x, float y) {
        float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (vertices.length - vertexIndex < 4 * VERTEX_SIZE || indices.length - shortIndex < 6) //
            flush();

        final float fx2 = x + texture.getWidth();
        final float fy2 = y + texture.getHeight();

        float color = this.colorBits;
        int idx = this.vertexIndex / VERTEX_SIZE;
        int shortIdx = this.shortIndex;
        indices[shortIdx++] = (short) (idx);
        indices[shortIdx++] = (short) (idx + 1);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 3);
        indices[shortIdx++] = (short) (idx);
        this.shortIndex = shortIdx;
        idx = this.vertexIndex;
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = 0;
        vertices[idx++] = 1;

        vertices[idx++] = x;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = 0;
        vertices[idx++] = 0;

        vertices[idx++] = fx2;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = 1;
        vertices[idx++] = 0;

        vertices[idx++] = fx2;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = 1;
        vertices[idx++] = 1;
        this.vertexIndex = idx;
    }

    /**
     * Draws a rectangle with the bottom left corner at x,y and stretching the region to cover the given width and height.
     */
    public void draw(Texture texture, float x, float y, float width, float height) {
        float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (vertices.length - vertexIndex < 4 * VERTEX_SIZE || indices.length - shortIndex < 6) //
            flush();

        final float fx2 = x + width;
        final float fy2 = y + height;
        final float u = 0;
        final float v = 1;
        final float u2 = 1;
        final float v2 = 0;

        float color = this.colorBits;
        int idx = this.vertexIndex / VERTEX_SIZE;
        int shortIdx = this.shortIndex;
        indices[shortIdx++] = (short) (idx);
        indices[shortIdx++] = (short) (idx + 1);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 3);
        indices[shortIdx++] = (short) (idx);
        this.shortIndex = shortIdx;
        idx = this.vertexIndex;
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;
        this.vertexIndex = idx;
    }

    /**
     * Draws a rectangle using the given vertices. There must be 4 vertices, each made up of 5 elements in this order: x, y, color,
     * u, v. The {@link #getColor()} from the SpriteBatch is not applied.
     */
    public void draw(Texture texture, float[] spriteVertices, int offset, int count) {
        int verticesLength = vertices.length;
        int remainingVertices = verticesLength;
        if (texture != lastTexture)
            switchTexture(texture);
        else {
            remainingVertices -= vertexIndex;
            if (remainingVertices == 0) {
                flush();
                remainingVertices = verticesLength;
            }
        }
        int copyCount = Math.min(remainingVertices, count);

        System.arraycopy(spriteVertices, offset, vertices, vertexIndex, copyCount);
        vertexIndex += copyCount;
        count -= copyCount;
        while (count > 0) {
            offset += copyCount;
            flush();
            copyCount = Math.min(verticesLength, count);
            System.arraycopy(spriteVertices, offset, vertices, 0, copyCount);
            vertexIndex += copyCount;
            count -= copyCount;
        }
    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the width and height of the region.
     */
    public void draw(TextureRegion region, float x, float y) {
        draw(region, x, y, region.getRegionWidth(), region.getRegionHeight());
    }

    /**
     * Draws a rectangle with the bottom left corner at x,y and stretching the region to cover the given width and height.
     */
    public void draw(TextureRegion region, float x, float y, float width, float height) {
        float[] vertices = this.vertices;

        Texture texture = region.getTexture();
        
        if (texture != lastTexture) {
            switchTexture(texture);
        } else if (vertices.length - vertexIndex < 4 * VERTEX_SIZE || indices.length - shortIndex < 6) //
            flush();

        final float fx2 = x + width;
        final float fy2 = y + height;
        final float u = region.getU();
        final float v = region.getV2();
        final float u2 = region.getU2();
        final float v2 = region.getV();

        float color = this.colorBits;
        int idx = this.vertexIndex / VERTEX_SIZE;
        int shortIdx = this.shortIndex;
        indices[shortIdx++] = (short) (idx);
        indices[shortIdx++] = (short) (idx + 1);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 3);
        indices[shortIdx++] = (short) (idx);
        this.shortIndex = shortIdx;
        idx = this.vertexIndex;

        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;
        this.vertexIndex = idx;
    }

    /**
     * Draws a rectangle with the bottom left corner at x,y and stretching the region to cover the given width and height. The
     * rectangle is offset by originX, originY relative to the origin. Scale specifies the scaling factor by which the rectangle
     * should be scaled around originX, originY. Rotation specifies the angle of counter clockwise rotation of the rectangle around
     * originX, originY.
     */
    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height,
                     float scaleX, float scaleY, float rotation) {
        float[] vertices = this.vertices;

        Texture texture = region.getTexture();
        if (texture != lastTexture) {
            switchTexture(texture);
        } else if (vertices.length - vertexIndex < 4 * VERTEX_SIZE || indices.length - shortIndex < 6) //
            flush();

        // bottom left and top right corner points relative to origin
        final float worldOriginX = x + originX;
        final float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;

        // scale
        if (scaleX != 1 || scaleY != 1) {
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }

        // construct corner points, start from top left and go counter clockwise
        final float p1x = fx;
        final float p1y = fy;
        final float p2x = fx;
        final float p2y = fy2;
        final float p3x = fx2;
        final float p3y = fy2;
        final float p4x = fx2;
        final float p4y = fy;

        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float x4;
        float y4;

        // rotate
        if (rotation != 0) {
            final float cos = MathUtils.cosDeg(rotation);
            final float sin = MathUtils.sinDeg(rotation);

            x1 = cos * p1x - sin * p1y;
            y1 = sin * p1x + cos * p1y;

            x2 = cos * p2x - sin * p2y;
            y2 = sin * p2x + cos * p2y;

            x3 = cos * p3x - sin * p3y;
            y3 = sin * p3x + cos * p3y;

            x4 = x1 + (x3 - x2);
            y4 = y3 - (y2 - y1);
        } else {
            x1 = p1x;
            y1 = p1y;

            x2 = p2x;
            y2 = p2y;

            x3 = p3x;
            y3 = p3y;

            x4 = p4x;
            y4 = p4y;
        }

        x1 += worldOriginX;
        y1 += worldOriginY;
        x2 += worldOriginX;
        y2 += worldOriginY;
        x3 += worldOriginX;
        y3 += worldOriginY;
        x4 += worldOriginX;
        y4 += worldOriginY;

        final float u = region.getU();
        final float v = region.getV2();
        final float u2 = region.getU2();
        final float v2 = region.getV();

        float color = this.colorBits;
        int idx = this.vertexIndex / VERTEX_SIZE;
        int shortIdx = this.shortIndex;
        indices[shortIdx++] = (short) (idx);
        indices[shortIdx++] = (short) (idx + 1);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 3);
        indices[shortIdx++] = (short) (idx);
        this.shortIndex = shortIdx;
        idx = this.vertexIndex;
        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = x3;
        vertices[idx++] = y3;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = x4;
        vertices[idx++] = y4;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;
        this.vertexIndex = idx;
    }

    /**
     * Draws a rectangle with the texture coordinates rotated 90 degrees. The bottom left corner at x,y and stretching the region
     * to cover the given width and height. The rectangle is offset by originX, originY relative to the origin. Scale specifies the
     * scaling factor by which the rectangle should be scaled around originX, originY. Rotation specifies the angle of counter
     * clockwise rotation of the rectangle around originX, originY.
     *
     * @param clockwise If true, the texture coordinates are rotated 90 degrees clockwise. If false, they are rotated 90 degrees
     *                  counter clockwise.
     */
    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height,
                     float scaleX, float scaleY, float rotation, boolean clockwise) {
        float[] vertices = this.vertices;

        Texture texture = region.getTexture();
        if (texture != lastTexture) {
            switchTexture(texture);
        } else if (vertices.length - vertexIndex < 4 * VERTEX_SIZE || indices.length - shortIndex < 6) //
            flush();

        // bottom left and top right corner points relative to origin
        final float worldOriginX = x + originX;
        final float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;

        // scale
        if (scaleX != 1 || scaleY != 1) {
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }

        // construct corner points, start from top left and go counter clockwise
        final float p1x = fx;
        final float p1y = fy;
        final float p2x = fx;
        final float p2y = fy2;
        final float p3x = fx2;
        final float p3y = fy2;
        final float p4x = fx2;
        final float p4y = fy;

        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float x4;
        float y4;

        // rotate
        if (rotation != 0) {
            final float cos = MathUtils.cosDeg(rotation);
            final float sin = MathUtils.sinDeg(rotation);

            x1 = cos * p1x - sin * p1y;
            y1 = sin * p1x + cos * p1y;

            x2 = cos * p2x - sin * p2y;
            y2 = sin * p2x + cos * p2y;

            x3 = cos * p3x - sin * p3y;
            y3 = sin * p3x + cos * p3y;

            x4 = x1 + (x3 - x2);
            y4 = y3 - (y2 - y1);
        } else {
            x1 = p1x;
            y1 = p1y;

            x2 = p2x;
            y2 = p2y;

            x3 = p3x;
            y3 = p3y;

            x4 = p4x;
            y4 = p4y;
        }

        x1 += worldOriginX;
        y1 += worldOriginY;
        x2 += worldOriginX;
        y2 += worldOriginY;
        x3 += worldOriginX;
        y3 += worldOriginY;
        x4 += worldOriginX;
        y4 += worldOriginY;

        float u1, v1, u2, v2, u3, v3, u4, v4;
        if (clockwise) {
            u1 = region.getU2();
            v1 = region.getV2();
            u2 = region.getU();
            v2 = region.getV2();
            u3 = region.getU();
            v3 = region.getV();
            u4 = region.getU2();
            v4 = region.getV();
        } else {
            u1 = region.getU();
            v1 = region.getV();
            u2 = region.getU2();
            v2 = region.getV();
            u3 = region.getU2();
            v3 = region.getV2();
            u4 = region.getU();
            v4 = region.getV2();
        }

        float color = this.colorBits;
        int idx = this.vertexIndex / VERTEX_SIZE;
        int shortIdx = this.shortIndex;
        indices[shortIdx++] = (short) (idx);
        indices[shortIdx++] = (short) (idx + 1);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 2);
        indices[shortIdx++] = (short) (idx + 3);
        indices[shortIdx++] = (short) (idx);
        this.shortIndex = shortIdx;
        idx = this.vertexIndex;
        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = color;
        vertices[idx++] = u1;
        vertices[idx++] = v1;

        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = x3;
        vertices[idx++] = y3;
        vertices[idx++] = color;
        vertices[idx++] = u3;
        vertices[idx++] = v3;

        vertices[idx++] = x4;
        vertices[idx++] = y4;
        vertices[idx++] = color;
        vertices[idx++] = u4;
        vertices[idx++] = v4;
        this.vertexIndex = idx;
    }

    /**
     * Draws a polygon region with the bottom left corner at x,y having the width and height of the region.
     */
    public void draw(PolygonRegion region, float x, float y) {
        final short[] triangles = this.indices;
        final short[] regionTriangles = region.getTriangles();
        final int regionTrianglesLength = regionTriangles.length;
        final float[] regionVertices = region.getVertices();
        final int regionVerticesLength = regionVertices.length;

        final Texture texture = region.getRegion().getTexture();
        if (texture != lastTexture)
            switchTexture(texture);
        else if (shortIndex + regionTrianglesLength > triangles.length || vertexIndex + regionVerticesLength > vertices.length)
            flush();

        int triangleIndex = this.shortIndex;
        int vertexIndex = this.vertexIndex;
        final int startVertex = vertexIndex / VERTEX_SIZE;

        for (int i = 0; i < regionTrianglesLength; i++)
            triangles[triangleIndex++] = (short) (regionTriangles[i] + startVertex);
        this.shortIndex = triangleIndex;

        final float[] vertices = this.vertices;
        final float color = this.colorBits;
        final float[] textureCoords = region.getTextureCoords();

        for (int i = 0; i < regionVerticesLength; i += 2) {
            vertices[vertexIndex++] = regionVertices[i] + x;
            vertices[vertexIndex++] = regionVertices[i + 1] + y;
            vertices[vertexIndex++] = color;
            vertices[vertexIndex++] = textureCoords[i];
            vertices[vertexIndex++] = textureCoords[i + 1];
        }
        this.vertexIndex = vertexIndex;
    }

    /**
     * Draws a polygon region with the bottom left corner at x,y and stretching the region to cover the given width and height.
     */
    public void draw(PolygonRegion region, float x, float y, float width, float height) {

        final short[] triangles = this.indices;
        final short[] regionTriangles = region.getTriangles();
        final int regionTrianglesLength = regionTriangles.length;
        final float[] regionVertices = region.getVertices();
        final int regionVerticesLength = regionVertices.length;
        final TextureRegion textureRegion = region.getRegion();

        final Texture texture = textureRegion.getTexture();
        if (texture != lastTexture)
            switchTexture(texture);
        else if (shortIndex + regionTrianglesLength > triangles.length || vertexIndex + regionVerticesLength > vertices.length)
            flush();

        int triangleIndex = this.shortIndex;
        int vertexIndex = this.vertexIndex;
        final int startVertex = vertexIndex / VERTEX_SIZE;

        for (int i = 0, n = regionTriangles.length; i < n; i++)
            triangles[triangleIndex++] = (short) (regionTriangles[i] + startVertex);
        this.shortIndex = triangleIndex;

        final float[] vertices = this.vertices;
        final float color = this.colorBits;
        final float[] textureCoords = region.getTextureCoords();
        final float sX = width / textureRegion.getRegionWidth();
        final float sY = height / textureRegion.getRegionHeight();

        for (int i = 0; i < regionVerticesLength; i += 2) {
            vertices[vertexIndex++] = regionVertices[i] * sX + x;
            vertices[vertexIndex++] = regionVertices[i + 1] * sY + y;
            vertices[vertexIndex++] = color;
            vertices[vertexIndex++] = textureCoords[i];
            vertices[vertexIndex++] = textureCoords[i + 1];
        }
        this.vertexIndex = vertexIndex;
    }

    /**
     * Draws the polygon region with the bottom left corner at x,y and stretching the region to cover the given width and height.
     * The polygon region is offset by originX, originY relative to the origin. Scale specifies the scaling factor by which the
     * polygon region should be scaled around originX, originY. Rotation specifies the angle of counter clockwise rotation of the
     * rectangle around originX, originY.
     */
    public void draw(PolygonRegion region, float x, float y, float originX, float originY, float width, float height,
                     float scaleX, float scaleY, float rotation) {

        final short[] triangles = this.indices;
        final short[] regionTriangles = region.getTriangles();
        final int regionTrianglesLength = regionTriangles.length;
        final float[] regionVertices = region.getVertices();
        final int regionVerticesLength = regionVertices.length;
        final TextureRegion textureRegion = region.getRegion();

        Texture texture = textureRegion.getTexture();
        if (texture != lastTexture)
            switchTexture(texture);
        else if (shortIndex + regionTrianglesLength > triangles.length || vertexIndex + regionVerticesLength > vertices.length)
            flush();

        int triangleIndex = this.shortIndex;
        int vertexIndex = this.vertexIndex;
        final int startVertex = vertexIndex / VERTEX_SIZE;

        for (int i = 0; i < regionTrianglesLength; i++)
            triangles[triangleIndex++] = (short) (regionTriangles[i] + startVertex);
        this.shortIndex = triangleIndex;

        final float[] vertices = this.vertices;
        final float color = this.colorBits;
        final float[] textureCoords = region.getTextureCoords();

        final float worldOriginX = x + originX;
        final float worldOriginY = y + originY;
        final float sX = width / textureRegion.getRegionWidth();
        final float sY = height / textureRegion.getRegionHeight();
        final float cos = MathUtils.cosDeg(rotation);
        final float sin = MathUtils.sinDeg(rotation);

        float fx, fy;
        for (int i = 0; i < regionVerticesLength; i += 2) {
            fx = (regionVertices[i] * sX - originX) * scaleX;
            fy = (regionVertices[i + 1] * sY - originY) * scaleY;
            vertices[vertexIndex++] = cos * fx - sin * fy + worldOriginX;
            vertices[vertexIndex++] = sin * fx + cos * fy + worldOriginY;
            vertices[vertexIndex++] = color;
            vertices[vertexIndex++] = textureCoords[i];
            vertices[vertexIndex++] = textureCoords[i + 1];
        }
        this.vertexIndex = vertexIndex;
    }

    /**
     * Draws the polygon using the given vertices and indices. Each vertices must be made up of 5 elements in this order: x, y,
     * color, u, v.
     */
    public void draw(Texture texture, float[] polygonVertices, int verticesOffset, int verticesCount, short[] polygonTriangles,
                     int trianglesOffset, int trianglesCount) {

        final short[] triangles = this.indices;
        final float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (shortIndex + trianglesCount > triangles.length || vertexIndex + verticesCount > vertices.length) //
            flush();

        int triangleIndex = this.shortIndex;
        final int vertexIndex = this.vertexIndex;
        final int startVertex = vertexIndex / VERTEX_SIZE;

        for (int i = trianglesOffset, n = i + trianglesCount; i < n; i++)
            triangles[triangleIndex++] = (short) (polygonTriangles[i] + startVertex);
        this.shortIndex = triangleIndex;

        System.arraycopy(polygonVertices, verticesOffset, vertices, vertexIndex, verticesCount);
        this.vertexIndex += verticesCount;
    }
}
