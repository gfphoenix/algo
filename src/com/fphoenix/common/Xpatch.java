package com.fphoenix.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 水平可伸缩图，由左中右三部分组成，左右两部分不拉神，中间的部分根据需要进行拉伸。
 * Width为三部分总长，也就是绘制的长度。假设左右两部分的宽度w相等（一般都是对称图形）。 1）当Width >
 * 2w时，中间部分需要绘制，绘制的宽度为(Width - 2w)； 2）当Width <=
 * 2w时，中间部分不需要绘制，且左右两侧的绘制宽度也需要裁剪，绘制宽度为Width/2 旋转点（ox，
 * oy）是相对左侧TextureRegion的左下角的坐标值。
 * 
 * @author wuhao
 * 
 */
public class Xpatch {
	private TextureRegion left = null;
	private TextureRegion mid = null;
	private TextureRegion right = null;

    private TextureRegion tmp;

	private float anchorX = 0.0f;
	private float anchorY = 0.5f;

	public int getHeight() {
		if (mid != null)
			return mid.getRegionHeight();
		return 0;
	}

	public static Xpatch create(TextureRegion tex, int leftWidth,
			int rightWidth) {
        if (tex==null) throw new NullPointerException("texture region is null");
		if (leftWidth <= 0 || rightWidth <= 0
				|| tex.getRegionWidth() <= leftWidth + rightWidth)
			throw new IllegalArgumentException("three part must have positive width");
		return new Xpatch(tex, leftWidth, rightWidth);
	}

	private Xpatch(TextureRegion tex, int leftWidth, int rightWidth) {
		int h = tex.getRegionHeight();
		int w = tex.getRegionWidth();

		left = new TextureRegion(tex, 0, 0, leftWidth, h);
		mid = new TextureRegion(tex, leftWidth, 0, w - leftWidth - rightWidth,
				h);
		right = new TextureRegion(tex, w - rightWidth, 0, rightWidth, h);
        tmp = new TextureRegion(tex);
	}

	private Xpatch(TextureRegion leftTex, TextureRegion midTex,
			TextureRegion rightTex) {
		this.left = leftTex;
		this.mid = midTex;
		this.right = rightTex;
	}

	public float getAnchorX() {
		return anchorX;
	}

	public float getAnchorY() {
		return anchorY;
	}

	public void setAnchorXY(float ax, float ay) {
		this.anchorX = ax;
		this.anchorY = ay;
	}

	public void setAnchorX(float ax) {
		this.anchorX = ax;
	}

	public void setAnchorY(float ay) {
		this.anchorY = ay;
	}

	public void draw(SpriteBatch sb, float x, float y, float width) {
		draw2(sb, x, y, width, 0f);
	}

	public void draw2(SpriteBatch sb, float x, float y, float width,
			float height) {

		int w1 = left.getRegionWidth();
		int w3 = right.getRegionWidth();
		if (width <= 0 || height < 0)
			return;

		float w2 = width - w1 - w3;
		float h = mid.getRegionHeight();
		if (height > 0f)
			h = height;
		x -= anchorX * width;
		y -= anchorY * h;
		if (w2 > 0) {
			sb.draw(left, x, y, left.getRegionWidth(), h);
			sb.draw(mid, x + w1, y, w2, h);
			sb.draw(right, x + w1 + w2, y, right.getRegionWidth(), h);
		} else {
			int leftWidth = (int) (w1 + w2 / 2);
			int rightWidth = (int) (w3 + w2 / 2);
			if (leftWidth > 0) {
                TextureRegion leftRegion = new TextureRegion(left, 0, 0, leftWidth, (int) h);
				sb.draw(leftRegion, x, y, leftRegion.getRegionWidth(), h);
			}
			if (rightWidth > 0) {
                TextureRegion rightRegion = new TextureRegion(right, (int) (-w2 / 2), 0,
						rightWidth, (int) h);
				sb.draw(rightRegion, x + leftWidth, y,
						rightRegion.getRegionWidth(), h);
			}
		}
	}

	public void draw(SpriteBatch sb, float x, float y, float width,
			float rotation) {
		draw(sb, x, y, width, rotation, 0f);
	}

	public void draw(SpriteBatch sb, float x, float y, float width,
			float rotation, float height) {
		if (rotation == 0.0f) {
			draw2(sb, x, y, width, height);
			return;
		}
		int w1 = left.getRegionWidth();
		int w3 = right.getRegionWidth();
		if (width <= 0 || height < 0f)
			return;
		float w2 = width - w1 - w3;
//		float h = mid.getRegionHeight();
		float h = height;
		if (height > 0) {
			h = height;
		}
		x -= anchorX * width;
		y -= anchorY * h;
		if (w2 > 0) {
			sb.draw(left, x, y, width / 2, h / 2, w1, h, 1, 1, rotation);
			sb.draw(mid, x + w1, y, (width - 2 * w1) / 2, h / 2, width - w1
					- w3, h, 1, 1, rotation);
			sb.draw(right, x + width - w3, y, (2 * w3 - width) / 2, h / 2, w3,
					h, 1, 1, rotation);
		} else {
			TextureRegion leftRegion = null;
			TextureRegion rightRegion = null;
			int leftWidth = (int) (w1 + w2 / 2);
			int rightWidth = (int) (w3 + w2 / 2);
			if (leftWidth > 0) {
				leftRegion = new TextureRegion(left, 0, 0, leftWidth, (int) h);
				sb.draw(leftRegion, x, y, width / 2, h / 2, w1 + w2 / 2, h, 1,
						1, rotation);
			}
			if (rightWidth > 0) {
				rightRegion = new TextureRegion(right, (int) (-w2 / 2), 0,
						rightWidth, (int) h);
				sb.draw(rightRegion, x + w1 + w2 / 2, y, (w3 - w1) / 2, h / 2,
						(w3 + w2 / 2), h, 1, 1, rotation);
			}
		}
	}
    public void drawScale(SpriteBatch sb, float x, float y, float w, float scaleX, float scaleY) {
        final float H = mid.getRegionHeight();
        float xx = x - w * anchorX;
        float yy = y - H * anchorY;
        int w1 = left.getRegionWidth();
        int w3 = right.getRegionWidth();
        if (w <= 0)
            return;
        float w2 = w - w1 -w3;
        if (w2>0) {
            sb.draw(left, xx, yy, x-xx, y-yy, w1, H, scaleX, scaleY, 0);
            xx += w1;
            sb.draw(mid, xx, yy, x-xx, y-yy, mid.getRegionWidth(), H, scaleX, scaleY, 0);
            xx += mid.getRegionWidth();
            sb.draw(right, xx, yy, x-xx, y-yy, w3, H, scaleX, scaleY, 0);
        }else {
            int leftWidth = (int) (w1 + w2 / 2);
            int rightWidth = (int) (w3 + w2 / 2);
            if (leftWidth > 0) {
                TextureRegion leftRegion = new TextureRegion(left, 0, 0, leftWidth, (int)H);
                sb.draw(leftRegion, xx, yy, x-xx, y-yy, w1 + w2 / 2, H, scaleX, scaleY, 0);
                xx += w1+w2/2;
            }
            if (rightWidth > 0) {
                TextureRegion rightRegion = new TextureRegion(right, (int) (-w2 / 2), 0,
                        rightWidth, (int) H);
                sb.draw(rightRegion, xx, yy, x-xx, y-yy, w3+w2/2, H, scaleX, scaleY, 0);
            }
        }
    }
    public void draw(SpriteBatch sb, float x, float y, float w, float scaleX, float scaleY, float rotation) {
        final float H = mid.getRegionHeight();
        float xx = x - w * anchorX;
        float yy = y - H * anchorY;
        int w1 = left.getRegionWidth();
        int w3 = right.getRegionWidth();
        if (w <= 0)
            return;
        float w2 = w - w1 -w3;
        if (w2>0) {
            sb.draw(left, xx, yy, x-xx, y-yy, w1, H, scaleX, scaleY, rotation);
            xx += w1;
            sb.draw(mid, xx, yy, x-xx, y-yy, w2, H, scaleX, scaleY, rotation);
            xx += w2;
            sb.draw(right, xx, yy, x-xx, y-yy, w3, H, scaleX, scaleY, rotation);
        }else {
            int leftWidth = (int) (w1 + w2 / 2);
            int rightWidth = (int) (w3 + w2 / 2);
            if (leftWidth > 0) {
                TextureRegion leftRegion = new TextureRegion(left, 0, 0, leftWidth, (int)H);
                sb.draw(leftRegion, xx, yy, x-xx, y-yy, w1 + w2 / 2, H, scaleX, scaleY, rotation);
                xx += w1+w2/2;
            }
            if (rightWidth > 0) {
                TextureRegion rightRegion = new TextureRegion(right, (int) (-w2 / 2), 0,
                        rightWidth, (int) H);
                sb.draw(rightRegion, xx, yy, x-xx, y-yy, w3+w2/2, H, scaleX, scaleY, rotation);
            }
        }
    }
    private TextureRegion getLeft(int w) {
        int H = mid.getRegionHeight();
        int x = left.getRegionX();
        int y = left.getRegionY();
        tmp.setRegion(x, y, w, H);
        return tmp;
    }
    private TextureRegion getRight(int w) {
        int H = mid.getRegionHeight();
        int x = right.getRegionX();
        int y = right.getRegionY();
        tmp.setRegion(x, y, w, H);
        return tmp;
    }
    // W total length, w drawC length
    public void drawCut(SpriteBatch batch, float x, float y, float W, float w, float scaleX, float scaleY, float rotation) {
        final float H = mid.getRegionHeight();
        float xx = x - W * anchorX;
        float yy = y - H * anchorY;
        int w1 = left.getRegionWidth();
        int w3 = right.getRegionWidth();
        if (w<=0) return;
        float L2 = W - w3;
        if (w<=w1) {
            TextureRegion region = getLeft((int)w);
            batch.draw(region, xx, yy, x-xx, y-yy, w, H, scaleX, scaleY, rotation);
        }else if (w<=L2) {
            batch.draw(left, xx, yy, x-xx, y-yy, w1, H, scaleX, scaleY, rotation);
            xx += w1;
            batch.draw(mid, xx, yy, x-xx, y-yy, w-w1, H, scaleX, scaleY, rotation);
        }else {
            batch.draw(left, xx, yy, x-xx, y-yy, w1, H, scaleX, scaleY, rotation);
            xx += w1;
            batch.draw(mid, xx, yy, x-xx, y-yy, L2-w1, H, scaleX, scaleY, rotation);

            float r3 = w - L2;
            TextureRegion region = getRight((int)r3);
//            TextureRegion region = new TextureRegion(right, 0, 0, (int)r3, (int)H);
            xx += L2-w1;
            batch.draw(region, xx, yy, x-xx, y-yy, r3, H, scaleX, scaleY, rotation);
        }
    }
}
