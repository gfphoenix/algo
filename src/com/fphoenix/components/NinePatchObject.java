package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by alan on 18-5-3.
 */

public class NinePatchObject {
    private TextureRegion tl = null;
    private TextureRegion ml = null;
    private TextureRegion bl = null;
    private TextureRegion tc = null;
    private TextureRegion mc = null;
    private TextureRegion bc = null;
    private TextureRegion tr = null;
    private TextureRegion mr = null;
    private TextureRegion br = null;

    public NinePatchObject setup(TextureRegion mother, int Wl, int Wr, int Ht, int Hb){
        assert mother!=null && Wl>=0 && Wr>=0 && Ht>=0 && Hb>=0
                && (Wl+Wr)<mother.getRegionWidth()
                && (Ht+Hb)<mother.getRegionHeight();
        int cw = mother.getRegionWidth() - Wl - Wr;
        int ch = mother.getRegionHeight() - Ht - Hb;
        if(Wl>0){
            if(Ht>0)
                this.tl = new TextureRegion(mother, 0,0, Wl, Ht);
            this.ml	= new TextureRegion(mother, 0, Ht, Wl, ch);
            if(Hb>0)
                this.bl = new TextureRegion(mother, 0, Ht+ch, Wl, Hb);
        }
        if(Wr>0){
            int xx = Wl+cw;
            if(Ht>0)
                this.tr = new TextureRegion(mother, xx, 0, Wr, Ht);
            this.mr = new TextureRegion(mother, xx, Ht, Wr, ch);
            if(Hb>0)
                this.br = new TextureRegion(mother, xx, Ht+ch, Wr, Hb);
        }
        if(Ht>0)
            this.tc = new TextureRegion(mother, Wl, 0, cw, Ht);
        if(Hb>0)
            this.bc = new TextureRegion(mother, Wl, Ht+ch, cw, Hb);
        this.mc = new TextureRegion(mother, Wl, Ht, cw, ch);

        return this;
    }

    public boolean hasLeft(){
        return this.ml != null;
    }
    public boolean hasRight(){
        return this.mr != null;
    }
    public boolean hasTop(){
        return this.tc != null;
    }
    public boolean hasBottom(){
        return this.bc != null;
    }
    public int getWidth(){
        int w = this.mc.getRegionWidth();
        if(hasLeft())
            w += this.ml.getRegionWidth();
        if(hasRight())
            w += this.mr.getRegionWidth();
        return w;
    }
    public int getHeight(){
        int h = this.mc.getRegionHeight();
        if(hasTop())
            h += this.tc.getRegionHeight();
        if(hasBottom())
            h += this.bc.getRegionHeight();
        return h;
    }
    public int getLeftWidth(){
        return hasLeft() ? this.ml.getRegionWidth() : 0;
    }
    public int getRightWidth(){
        return hasRight() ? this.mr.getRegionWidth() : 0;
    }
    public int getTopHeight(){
        return hasTop() ? this.tc.getRegionHeight() : 0;
    }
    public int getBottomHeight(){
        return hasBottom() ? this.bc.getRegionHeight() : 0;
    }

    // widht > Wl + Wr && height > Ht + Hb
    // must have center region
    private void drawGtGt(SpriteBatch batch, float ox, float oy, int width, int height){
//    	float ox = x - width * anchorX;
//    	float oy = y - height * anchorY;
        boolean lb = hasLeft();
        boolean rb = hasRight();
        boolean tb = hasTop();
        boolean bb = hasBottom();

        int Ht = getTopHeight();
        int Hb = getBottomHeight();
        int Wl = getLeftWidth();
        int Wr = getRightWidth();
        float hc = height - (Ht+Hb);
        float wc = width - (Wl+Wr);
        // left three regions
        if(lb){
            if(bb){
                batch.draw(this.bl, ox, oy);
            }

            batch.draw(this.ml, ox, oy+Hb, Wl, hc);
            if(tb){
                batch.draw(this.tl, ox, oy+Hb+hc);
            }
        }
        // middle three regions
        {
            float xx = ox + Wl;
            if (bb) {
                batch.draw(this.bc, xx, oy, wc, Hb);
            }
            batch.draw(this.mc, xx, oy + Hb, wc, hc);
            if (tb) {
                batch.draw(this.tc, xx, oy + Hb + hc, wc, Ht);
            }
        }
        if(rb){
            float xx = ox+Wl+wc;
            if(bb){
                batch.draw(this.br, xx, oy);
            }
            batch.draw(this.mr, xx, oy+Hb, Wr, hc);
            if(tb){
                batch.draw(this.tr, xx, oy+Hb+hc);
            }
        }
    }

    // TL  TC  TR
    // BL  BC  BR
    // middle row is missing height<=Ht+Hb
    // the height of top row and bottom row maybe scaled small, need to
    private void drawGtLe(SpriteBatch batch, float ox, float oy, int width, int height){
//    	float ox = x - width * anchorX;
//    	float oy = y - height * anchorY;
        boolean lb = hasLeft();
        boolean rb = hasRight();

        int Ht = getTopHeight();
        int Hb = getBottomHeight();
        int Wl = getLeftWidth();
        int Wr = getRightWidth();
        float wc = width - (Wl+Wr);


        int H = Ht + Hb;
        float ht = ((float)Ht * height)/(float)H;
        float hb = ((float)Hb * height)/(float)H;
        int hbz = (int)hb;
        int htz = (int)ht;
        if(hbz>0){
            int offy = Ht - hbz;
            if(lb){
                TextureRegion bottomLeft = new TextureRegion(this.bl, 0, offy, Wl, hbz);
                batch.draw(bottomLeft, ox, oy);
            }
            TextureRegion bottomCenter = new TextureRegion(this.bc, 0, offy,
                    this.bc.getRegionWidth(), hbz);
            batch.draw(bottomCenter, ox+Wl, oy, wc, hb);
            if(rb){
                TextureRegion bottomRight = new TextureRegion(this.br, 0, offy, Wr, hbz);
                batch.draw(bottomRight, ox+Wl+wc, oy);
            }
        }
        if(htz>0){
            float yy = oy+hb;
            if(lb){
                TextureRegion topLeft = new TextureRegion(this.tl, 0, 0, Wl, htz);
                batch.draw(topLeft, ox, yy);
            }
            TextureRegion topCenter = new TextureRegion(this.tc, 0, 0,
                    this.tc.getRegionWidth(), htz);
            batch.draw(topCenter, ox+Wl, yy, wc, ht);
            if(rb){
                TextureRegion topRight = new TextureRegion(this.tr, 0, 0,
                        Wr, htz);
                batch.draw(topRight, ox+Wl+wc, yy);
            }
        }

    }

    // TL  TR
    // ML  MR
    // BL  BR
    private void drawLeGt(SpriteBatch batch, float ox, float oy, int width, int height){
//    	float ox = x - width * anchorX;
//    	float oy = y - height * anchorY;
        boolean bb = hasBottom();
        boolean tb = hasTop();

        int Ht = getTopHeight();
        int Hb = getBottomHeight();
        int Wl = getLeftWidth();
        int Wr = getRightWidth();
        float hc = height - (Ht+Hb);

        int W = Wl + Wr;
        float wl = ((float)Wl*width)/(float)W;
        float wr = ((float)Wr*width)/(float)W;
        int wlz = (int)wl;
        int wrz = (int)wr;
        if(wlz>0){
            if(bb){
                TextureRegion bottomLeft = new TextureRegion(this.bl, 0, 0, wlz, Hb);
                batch.draw(bottomLeft, ox, oy, wl, Hb);
            }
            TextureRegion midLeft = new TextureRegion(this.ml, 0, 0,
                    wlz, this.ml.getRegionHeight());
            batch.draw(midLeft, ox, oy+Hb, wl, hc);
            if(tb){
                TextureRegion topLeft = new TextureRegion(this.tl, 0, 0, wlz, Ht);
                batch.draw(topLeft, ox, oy+Hb+hc, wl, Ht);
            }
        }
        if(wrz>0){
            float xx = ox+wl;
            int offX = (int)(Wr-wr);
            if(bb){
                TextureRegion bottomLeft = new TextureRegion(this.br, offX, 0, wrz, Hb);
                batch.draw(bottomLeft, xx, oy);
            }
            TextureRegion bottomCenter = new TextureRegion(this.mr, offX, 0, wrz, this.mr.getRegionHeight());
            batch.draw(bottomCenter, xx, oy+Hb, wr, hc);
            if(tb){
                TextureRegion topRight = new TextureRegion(this.tr, offX, 0, wrz, Ht);
                batch.draw(topRight, xx, oy+Hb+hc);
            }
        }

    }
    private void drawLeLe(SpriteBatch batch, float ox, float oy, int width, int height){
//    	float ox = x - width * anchorX;
//    	float oy = y - height * anchorY;
        int Wl = getLeftWidth();
        int Wr = getRightWidth();
        int Ht = getTopHeight();
        int Hb = getBottomHeight();

        float wl = ((float)Wl*width)/(float)(Wl+Wr);
        float wr = ((float)Wr*width)/(float)(Wl+Wr);
        float ht = ((float)Ht*height)/(float)(Ht+Hb);
        float hb = ((float)Hb*height)/(float)(Ht+Hb);

        int wlz = (int)wl;
        int wrz = (int)wr;
        int htz = (int)ht;
        int hbz = (int)hb;
        if(wlz>0){
            if(hbz>0){
                TextureRegion bottomLeft = new TextureRegion(this.bl,
                        0, Hb-hbz, wlz, hbz);
                batch.draw(bottomLeft, ox, oy);
            }
            if(htz>0){
                TextureRegion topLeft = new TextureRegion(this.tl,
                        0,0, wlz, htz);
                batch.draw(topLeft, ox, oy+hb);
            }
        }
        if(wrz>0){
            float xx = ox+wl;
            if(hbz>0){
                TextureRegion bottomRight = new TextureRegion(this.br,
                        Wr-wrz, Hb-hbz, wrz, hbz);
                batch.draw(bottomRight, xx, oy);
            }
            if(htz>0){
                TextureRegion topRight = new TextureRegion(this.tr,
                        Wr-wrz, 0, wrz, htz);
                batch.draw(topRight, xx, oy+hb);
            }
        }
    }
    public void draw(SpriteBatch batch, float x, float y, int width, int height){
        draw(batch, x, y, width, height, .5f, .5f);
    }
    public void draw(SpriteBatch batch, float x, float y, int width, int height, float anchorX, float anchorY){
        int w = getLeftWidth() + getRightWidth();
        int h = getTopHeight() + getBottomHeight();
        float ox = x - width * anchorX;
        float oy = y - height * anchorY;
        if(width>w){
            if(height>h)
                drawGtGt(batch, ox, oy, width, height);
            else
                drawGtLe(batch, ox, oy, width, height);
        }else{
            if(height>h)
                drawLeGt(batch, ox, oy, width, height);
            else
                drawLeLe(batch, ox, oy, width, height);
        }
    }
}
