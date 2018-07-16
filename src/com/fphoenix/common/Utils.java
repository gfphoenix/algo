package  com.fphoenix.common;

import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.fphoenix.common.actor.AnchorActor;
import com.fphoenix.common.actor.ScalableAnchorActor;


public class Utils {
	private Utils(){}
	public static final long OneDay = 24 * 60 * 60 * 1000;
	public static int random(int n){
		return (int)(Math.random()*n);
	}
	public static float random(float a, float b){
		double r = Math.random();
		return (float) (a+(b-a)*r);
	}
	public static double random(double a, double b){
		double r = Math.random();
		return  a+(b-a)*r;
	}

	// range >0
	public static int round(float v, int range) {
		int a = (int) (v / range);
		final int X = a * range;
		float q = v - X;

		if (Math.abs(q) * 2 < range) {
			return X;
		}
		if (q > 0)
			return X + range;
		return X - range;
	}
	public static StringBuilder appendHMS(StringBuilder sb, long secs) {
		long s = secs % 60; secs/=60;
		long m = secs % 60; secs/=60;
		long h = secs;
		if (h<10) sb.append('0');
		sb.append(h).append(':');
		if (m<10) sb.append('0');
		sb.append(m).append(':');
		if (s<10) sb.append('0');
		sb.append(s);
		return sb;
	}
	public static BaseGame getBaseGame(){
		return (BaseGame)Gdx.app.getApplicationListener();
	}
	public static AssetManager getAssetManager(){
		return getBaseGame().getAssetManager();
	}
	public static TextureAtlas getTextureAtlas(String file){
		return getAssetManager().get(file);
	}
	public static void load(String file, Class<?>type){
		AssetManager am = getAssetManager();
        if (!am.isLoaded(file, type)) {
            am.load(file, type);
        }
	}
	public static void unload(String file){
		try{
		AssetManager am = getAssetManager();
		if(am.isLoaded(file)){
			am.unload(file);
		}
		}catch(Throwable t){
            t.printStackTrace();
        }
	}
    public static void unload(String ...files) {
        for (String file : files)
            unload(file);
    }
    public static TextureRegion findRegion(String name, TextureAtlas ...atlases) {
        TextureRegion region=null;
        for (TextureAtlas ta : atlases)
            if ((region=ta.findRegion(name))!=null)
                return region;
        return null;
    }
	// 
	public static TextureAtlas load_get(String file){
		AssetManager am = getAssetManager();
        if (!am.isLoaded(file)) {
            am.load(file, TextureAtlas.class);
            am.finishLoading();
        }
		return am.get(file, TextureAtlas.class);
	}
	public static BitmapFont load_get_fnt(String file) {
		AssetManager am = getAssetManager();
		BitmapFont fnt = null;
		if (!am.isLoaded(file)) {
			am.load(file, BitmapFont.class);
			am.finishLoading();
			fnt = am.get(file, BitmapFont.class);
			Texture texture = fnt.getRegion().getTexture();
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		}
		return fnt!=null ? fnt : am.get(file, BitmapFont.class);
	}
	public static void finishLoading(){
		getAssetManager().finishLoading();
	}
	public static Array<TextureRegion> loadTextureA(String atlas, String prefix){
		AssetManager am = Utils.getAssetManager();
		am.load(atlas, TextureAtlas.class);
		am.finishLoading();
		TextureAtlas ta = am.get(atlas, TextureAtlas.class);
		return loadTextureA(ta, prefix);
	}
	public static Array<TextureRegion> loadTextureA(TextureAtlas ta, String prefix){
		Array<TextureRegion> arr = new Array<TextureRegion>();
		for(int i=0; ; i++){
			TextureRegion r = ta.findRegion(prefix+i);
			if(r==null)
				break;
			arr.add(r);
		}
		return arr;
	}
	public static Vector2 clampRadius(Vector2 xy, float radius) {
		float x = xy.x, y=xy.y;
		float d2 = x*x+y*y;
		if (d2<=radius*radius) return xy;
		return xy.scl(radius/(float)(Math.sqrt(d2)));
	}
	// (0,0, z) = v1 x v2
	public static float cross(Vector2 v1, Vector2 v2) {
		return v1.x * v2.y - v1.y * v2.x;
	}
	public static float cross(float x1, float y1, float x2, float y2) {
		return x1 * y2 - x2 * y1;
	}
	// (xy, 0) x (0, 0, z)
	public static Vector2 cross(Vector2 xy, float z) {
		return xy.set(xy.y*z, -xy.x*z);
	}
	// (0,0,z) x (xy, 0)
	public static Vector2 cross(float z, Vector2 xy) {
		return xy.set(-z*xy.y, z*xy.x);
	}
	public static int sign(int a){
		if(a==0)
			return 0;
		if(a>0)
			return 1;
		return -1;
	}
	public static void captureTouch(Actor actor){
		InputListener l = new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				event.setBubbles(false);
				return true;
			}
		};
		
		actor.addListener(l);
	}
	public static void toLowerCase(char []chars){
		for(int i=0; i<chars.length; i++){
			char c = chars[i];
			if(c>='A' && c<='Z')
				chars[i] = (char)(c-'A'+'a');
		}
	}
	public static void toUpperCase(char []chars){
		for(int i=0; i<chars.length; i++){
			char c = chars[i];
			if(c>='a' && c<='z')
				chars[i] = (char)(c-'a'+'A');
		}
	}
	public static char [] newLowerCase(final char []chs){
		char []tmp = new char[chs.length];
		System.arraycopy(chs, 0, tmp, 0, tmp.length);
		toLowerCase(tmp);
		return tmp;
	}
	public static char [] newUpperCase(final char []chs){
		char []tmp = new char[chs.length];
		System.arraycopy(chs, 0, tmp, 0, tmp.length);
		toUpperCase(tmp);
		return tmp;
	}
	public static boolean isYestoday(long x, long yestoday){
		return CompareDay(x, yestoday+OneDay)==0;
	}
	public static int CompareDay(long a, long b){
		Date da = new Date(a);
		Date db = new Date(b);
		
		int r;
		r = da.getYear() - db.getYear();
		if (r!=0)
			return r;
		r = da.getMonth() - db.getMonth();
		if (r!=0)
			return r;
		return da.getDate() - db.getDate();
	}
	public static boolean inSameDay(long a, long b){
		return CompareDay(a, b)==0;
	}
	public static ScalableAnchorActor createMask(TextureAtlas ta){
		return createMask(ta, "maskPlane");
	}
	public static ScalableAnchorActor createMask(TextureAtlas ta, String name){
		ScalableAnchorActor mask = new ScalableAnchorActor(ta.findRegion("maskPlane"));
		mask.setSize(Config.width, Config.height);
		mask.setAnchor(0, 0);
		mask.setPosition(0, 0);
		mask.setColor(.025f, .025f, .025f, .75f);
		captureTouch(mask);
		return mask;
	}
	public static Action floatUpRemove(float dy, float dt){
		Action m = Actions.moveBy(0, dy, dt);
		Action fade = Actions.fadeOut(dt);
		Action para = Actions.parallel(m, fade);
		Action seq = Actions.sequence(para, Actions.removeActor());
		return seq;
	}
	
	public static TextureRegion NewTextureRegion(Texture t, int x, int y, int width, int height){
		TextureRegion r = new TextureRegion(t);
		int w = Math.abs(width);
		int h = Math.abs(height);
		float u = (float)((double)x/t.getWidth());
		float v = (float)((double)y/t.getHeight());
		float u2 = (float)((double)(x+width)/t.getWidth());
		float v2 = (float)((double)(y+height)/t.getHeight());

		r.setRegionWidth(w);
		r.setRegionHeight(h);
		r.setU(u);
		r.setV(v);
		r.setU2(u2);
		r.setV2(v2);
		
		return r;
	}
	public static boolean isIntersect(Rectangle r1, Rectangle r2){
		return r1.overlaps(r2);
	}
	static Vector2 tmpV1 = new Vector2();
	static Vector2 tmpV2 = new Vector2();
	// 点P0投影到直线P1-P2上的坐标是
	// P=P1 + ((P0-P1)*(P2-P1)/|P2-P1|^2)*(P2-P1)
	public static Vector2 project(final double p1x, final double p1y, final double p2x, final double p2y, Vector2 point){
		// v1 = P0 - P1
		double v1x = point.x - p1x;
		double v1y = point.y - p1y;
		// v2 = P2 - P1
		double v2x = p2x - p1x;
		double v2y = p2y - p1y;
		
		double dot = v1x * v2x + v1y * v2y;
		double mod2 = v2x * v2x + v2y * v2y;
		double mul = dot/mod2;
		point.x = (float)(p1x + mul * v2x);
		point.y = (float)(p1y + mul * v2y);
		return point;
	}
	// box1 is rotated, box2 is aabb
	// ax and ay is the anchor point, which is used to determine the rotation point
	private static Vector2 transform(float c, float s, Vector2 p){
		float xx = c * p.x - s * p.y;
		float yy = s * p.x + c * p.y;
		return p.set(xx, yy);
	}
	private static Vector2 rotate(float cx, float cy, float C, float S, Vector2 p){
		float vx = p.x - cx;
		float vy = p.y - cy;
		p.x = cx + C * vx - S * vy;
		p.y = cy + S * vx + C * vy;
		return p;
	}
	protected static Vector2 rotate(float cx, float cy, float theta, Vector2 p){
		float vx = p.x - cx;
		float vy = p.y - cy;
		float C = MathUtils.cosDeg(theta);
		float S = MathUtils.sinDeg(theta);
		p.x = cx + C * vx - S * vy;
		p.y = cy + S * vx + C * vy;
		return p;
	}
	// (vx, vy)是直线的方向向量， （StartX，StartY）是一个端点，并且方向向量以它为起点。
	private static Vector2 project2(float vx, float vy, float StartX, float StartY, Vector2 p){
		float dx = p.x - StartX;
		float dy = p.y - StartY;
		float dot = dx * vx + dy * vy;
		float mod2 = vx * vx + vy * vy;
		p.x = StartX + dot * vx / mod2;
		p.y = StartY + dot * vy / mod2;
		return p;
	}
	// 1. 计算旋转后的点，P0, P1, P2, P3
	// 2. 测试投影是否有交集
	public static boolean isIntersect(Rectangle r1, float ax, float ay,float theta1, Rectangle r2){
		float cx = r1.x + r1.width * ax;
		float cy = r1.y + r1.height * ay;
		float c = MathUtils.cosDeg(theta1);
		float s = MathUtils.sinDeg(theta1);
		
		float x1,y1, x2,y2, x3,y3, x4,y4;
		Vector2 P;
		P = rotate(cx, cy, c, s, tmpV1.set(r1.x, r1.y));
		x1 = P.x;
		y1 = P.y;
		P = rotate(cx, cy, c, s, tmpV1.set(r1.x+r1.width, r1.y));
		x2 = P.x;
		y2 = P.y;
		P = rotate(cx, cy, c, s, tmpV1.set(r1.x, r1.y+r1.height));
		x3 = P.x;
		y3 = P.y;
		x4 = x2 + x3 - x1;
		y4 = y2 + y3 - y1;
		
		float X1 = r2.x;
		float X2 = r2.x + r2.width;
		float Y1 = r2.y;
		float Y2 = r2.y + r2.height;
		boolean overlap = false;
		overlap = overlap || (x1>=X1 && x1<=X2); 
		overlap = overlap || (x2>=X1 && x2<=X2); 
		overlap = overlap || (x3>=X1 && x3<=X2); 
		overlap = overlap || (x4>=X1 && x4<=X2); 
		if(overlap == false)
			return false;
		overlap = false;
		overlap = overlap || (y1>=Y1 && y1<=Y2); 
		overlap = overlap || (y2>=Y1 && y2<=Y2); 
		overlap = overlap || (y3>=Y1 && y3<=Y2); 
		overlap = overlap || (y4>=Y1 && y4<=Y2); 
		if(overlap == false)
			return false;
		float p1x, p1y, p2x, p2y, p3x, p3y, p4x, p4y;
		// 点P0投影到直线P1-P2上的坐标是
		// P=P1 + (((P0-P1)*(P2-P1))/|P2-P1|^2)*(P2-P1)
		// 两个方向的投影：P1-P2, P1-P3
		// P1-P2
		float vx = x2 - x1;
		float vy = y2 - y1;
		P = project2(vx, vy, x1, y1, tmpV1.set(X1, Y1));
		p1x = P.x; p1y = P.y;
		P = project2(vx, vy, x1, y1, tmpV1.set(X2, Y1));
		p2x = P.x; p2y = P.y;
		P = project2(vx, vy, x1, y1, tmpV1.set(X1, Y2));
		p3x = P.x; p3y = P.y;
		p4x = p2x+p3x-p1x;
		p4y = p2y+p3y-p1y;
		overlap = false;
		overlap = overlap || (((x1<=p1x && p1x<=x2)||(x1>=p1x && p1x>=x2)) && ((y1<=p1y && p1y<=y2)||(y1>=p1y && p1y>=y2)));
		overlap = overlap || (((x1<=p2x && p2x<=x2)||(x1>=p2x && p2x>=x2)) && ((y1<=p2y && p2y<=y2)||(y1>=p2y && p2y>=y2)));
		overlap = overlap || (((x1<=p3x && p3x<=x2)||(x1>=p3x && p3x>=x2)) && ((y1<=p3y && p3y<=y2)||(y1>=p3y && p3y>=y2)));
		overlap = overlap || (((x1<=p4x && p4x<=x2)||(x1>=p4x && p4x>=x2)) && ((y1<=p4y && p4y<=y2)||(y1>=p4y && p4y>=y2)));

		if(!overlap)
			return false;
		vx = x3 - x1;
		vy = y3 - y1;
		P = project2(vx, vy, x1, y1, tmpV1.set(X1, Y1));
		p1x = P.x; p1y = P.y;
		P = project2(vx, vy, x1, y1, tmpV1.set(X2, Y1));
		p2x = P.x; p2y = P.y;
		P = project2(vx, vy, x1, y1, tmpV1.set(X1, Y2));
		p3x = P.x; p3y = P.y;
		p4x = p2x+p3x-p1x;
		p4y = p2y+p3y-p1y;
		overlap = false;
		overlap = overlap || (((x1<=p1x && p1x<=x3)||(x1>=p1x && p1x>=x3)) && ((y1<=p1y && p1y<=y3)||(y1>=p1y && p1y>=y3)));
		overlap = overlap || (((x1<=p2x && p2x<=x3)||(x1>=p2x && p2x>=x3)) && ((y1<=p2y && p2y<=y3)||(y1>=p2y && p2y>=y3)));
		overlap = overlap || (((x1<=p3x && p3x<=x3)||(x1>=p3x && p3x>=x3)) && ((y1<=p3y && p3y<=y3)||(y1>=p3y && p3y>=y3)));
		overlap = overlap || (((x1<=p4x && p4x<=x3)||(x1>=p4x && p4x>=x3)) && ((y1<=p4y && p4y<=y3)||(y1>=p4y && p4y>=y3)));

		if(!overlap)
			return false;
		
		// all projection overlaps, so return 
		return true;
	}
	static Vector2 []src = new Vector2[4];
	static Vector2 []dst = new Vector2[4];
	static {
		for(int i=0; i<4; i++){
			src[i] = new Vector2();
			dst[i] = new Vector2();
		}
	}
	private static Vector2[] toVector(Vector2 []v, Rectangle r, float ax, float ay, float theta){
		float c = MathUtils.cosDeg(theta);
		float s = MathUtils.sinDeg(theta);
		float cx = r.x + ax * r.width;
		float cy = r.y + ay * r.height;
		float x = r.x - cx;
		float y = r.y - cy;
		v[0].x = c * x - s * y + cx;
		v[0].y = s * x + c * y + cy;
		x = r.x + r.width - cx;
		y = r.y - cy;
		v[1].x = c * x - s * y + cx;
		v[1].y = s * x + c * y + cy;
		x = r.x - cx;
		y = r.y + r.height - cy;
		v[2].x = c * x - s * y + cx;
		v[2].y = s * x + c * y + cy;
		x = r.x + r.width - cx;
		y = r.y + r.height - cy;
		v[3].x = c * x - s * y + cx;
		v[3].y = s * x + c * y + cy;
		return v;
	}
	
	private static boolean overlaps2(Vector2 p1, Vector2 p2, Vector2 []set){
		float dx = p2.x - p1.x;
		float dy = p2.y - p1.y;
		float mod2 = dx*dx + dy*dy;
		boolean ok = false;
		float minX = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = Float.MIN_VALUE;
		float xa,xb,ya,yb;
		if(p1.x < p2.x){
			xa = p1.x;
			xb = p2.x;
		}else{
			xa = p2.x;
			xb = p1.x;
		}
		if(p1.y < p2.y){
			ya = p1.y;
			yb = p2.y;
		}else{
			ya = p2.y;
			yb = p1.y;
		}
		for(int i=0; i<4 && !ok; i++){
			float tx = set[i].x - p1.x;
			float ty = set[i].y - p1.y;
			float dot = tx * dx + ty * dy;
			float x = p1.x + dot * dx / mod2;
			float y = p1.y + dot * dy / mod2;
			if(x<minX)
				minX = x;
			if(x>maxX){
				maxX = x;
			}
			if(y<minY)
				minY = y;
			if(y>maxY)
				maxY = y;
			ok = maxX>=xa && minX<=xb && maxY>=ya && minY<=yb;
		}
		
		return ok;
	}
	private static boolean overlaps(Vector2 p1, Vector2 p2, Vector2 []set){
		boolean left=false;
		boolean mid = false;
		boolean right = false;
		final float dx = p2.x - p1.x;
		final float dy = p2.y - p1.y;
		for(int i=0; i<4; i++){
			float tx = set[i].x - p1.x;
			float ty = set[i].y - p1.y;
			float dot = dx * tx + dy * ty;
			if(dot<=0){
				left = true;
				continue;
			}
			tx = tx - dx;
			ty = ty - dy;
			dot = (-dx)*tx + (-dy) * ty;
			if(dot<=0)
				right = true;
			else
				mid = true;
		}
		return (mid || (left && right));
	}

	// r1没有旋转， r2有旋转
	public static boolean isIntersect(Rectangle r1, Rectangle r2, float ax, float ay, float theta){
		boolean ok = false;
		Vector2 []a = toVector(src, r2, ax, ay, theta);
		float minX, minY, maxX, maxY;
		minX = maxX = a[0].x;
		minY = maxY = a[0].y;
		float xa = r1.x;
		float xb = r1.x + r1.width;
		float ya = r1.y;
		float yb = r1.y+r1.height;
		for(int i=1; i<4 && !ok; i++){
			minX = Math.min(minX, a[i].x);
			maxX = Math.max(maxX, a[i].x);
			ok = maxX>=xa && minX<=xb;
		}
		if(!ok)
			return false;
		ok = false;
		for(int i=1; i<4 && !ok; i++){
			minY = Math.min(minY, a[i].y);
			maxY = Math.max(maxY, a[i].y);
			ok = maxY>=ya && minY<=yb;
		}
		if(!ok)
			return false;
		Vector2 []b = dst;
		b[0].set(r1.x, r1.y);
		b[1].set(r1.x+r1.width, r1.y);
		b[2].set(r1.x, r1.y+r1.height);
		b[3].set(r1.x+r1.width, r1.y+r1.height);
		ok = ok && overlaps(a[0], a[1], b);
		ok = ok && overlaps(a[0], a[2], b);
		return ok;
	}
	// [0]=BL, [1]=BR, [2]=TL, [3]=TR
	public static boolean isIntersect(Rectangle r1, float ax, float ay, float theta1, Rectangle r2, float ax2, float ay2, float theta2){
		if(theta1==0f && theta2==0f){
			return r2.overlaps(r1);
		}
		if(theta1==0f){
			return isIntersect(r1, r2, ax2, ay2, theta2);
		}
		if(theta2==0f){
			return isIntersect(r2, r1, ax, ay, theta1);
		}
		Vector2 []a = toVector(src, r1, ax, ay, theta1);
		Vector2 []b = toVector(dst, r2, ax2, ay2, theta2);
		boolean ok = overlaps(a[0], a[1], b);
		ok = ok && overlaps(a[0], a[2], b);
		ok = ok && overlaps(b[0], b[1], a);
		ok = ok && overlaps(b[0], b[2], a);
		return ok;
	}
	public static boolean isIntersect0(Rectangle r1, float ax, float ay, float theta1, Rectangle r2, float ax2, float ay2, float theta2){
		if(theta1==0f || theta2==0f){
			if(theta1==0f && theta2==0f){
				return r2.overlaps(r1);
			}
			if(theta1==0f){
				return isIntersect(r2, ax2, ay2, theta2, r1);
			}else{
				return isIntersect(r1, ax, ay, theta1, r2);
			}
		}
		// both rectangles are rotated.
		System.out.printf("Both rectangles are rotated...<%f, %f>\n", theta1, theta2);
		
		return true;
	}
	static class Box {
		Rectangle rect;
		float rotation;
		float ax, ay;
		Box(){
			rect = new Rectangle();
		}
	}
	static Box box1 = new Box();
	static Box box2 = new Box();
	static Box toBox(AnchorActor aa, Box box){
		if(box==null){
			box = new Box();
		}
		box.rect.x = aa.getX() - aa.getAnchorX()*aa.getWidth();
		box.rect.y = aa.getY() - aa.getAnchorY()*aa.getHeight();
		box.rect.width = aa.getWidth();
		box.rect.height = aa.getHeight();
		box.rotation = aa.getRotation();
		box.ax = aa.getAnchorX();
		box.ay = aa.getAnchorY();
		return box;
	}
	public static boolean isIntersect(AnchorActor a, AnchorActor b){
		Box ba = toBox(a, box1);
		Box bb = toBox(b, box2);
		boolean ok = isIntersect(
				ba.rect, ba.ax, ba.ay, ba.rotation, 
				bb.rect, bb.ax, bb.ay, bb.rotation);
		return ok;
	}
	public static boolean isIntersect(Box ba, Box bb){
		return isIntersect(
				ba.rect, ba.ax, ba.ay, ba.rotation, 
				bb.rect, bb.ax, bb.ay, bb.rotation);
	}
	public static void testProj(){
		float [] line = new float[4];
		Vector2 p=new Vector2();
		for(int i=100; i>0; i--){
			for(int j=0; j<line.length; j++){
				line[j] = MathUtils.random(200f);
			}
			float x = MathUtils.random(200f);
			float y = MathUtils.random(200f);
			p = project(line[0], line[1], line[2], line[3], p.set(x, y));
			double dx = line[2]-line[0];
			double dy = line[3]-line[1];
			double sx = p.x-line[0];
			double sy = p.y-line[1];
			double tx = x-p.x;
			double ty = y-p.y;
			double m1 = (dx*dx+dy*dy)*(sx*sx+sy*sy);
			double m2 = (dx*dx+dy*dy)*(tx*tx+ty*ty);
			double dot1 = sx*dx+sy*dy; // +-1
			float theta1 = (float)(dot1/Math.sqrt(m1));
			double dot2 = tx*dx + ty*dy; // 0
			float theta2 = (float)(dot2/Math.sqrt(m2));
			System.out.printf("dot1 = %f, dot2 = %f\n", theta1, theta2);
		}
	}
	public static void testOOB(){
		Rectangle r1 = new Rectangle();
		Rectangle r2 = new Rectangle();
		r1.set(0, 0, 60, 60);
		r2.set(30, 30, 60, 10);
		boolean ok = isIntersect(r1, 0, 0, 0, r2, .5f, .5f, 30);
		System.out.println("ok1 = "+ok);
		ok = isIntersect(r1, 0, 0, 0, r2, .5f, .5f, 0);
		System.out.println("ok2 = "+ok);
		r2.set(20, 65, 100, 10);
		for(int i=-360; i<360; i++){
			ok = isIntersect(r1, 0, 0, 0, r2, .5f, .5f, i);
			System.out.printf("rotate %d ok = %b\n", i, ok);
		}
		ok = isIntersect(r1, 0, 0, 0, r2, .5f, .5f, 135);
		System.out.println("ok3 = "+ok);
		ok = isIntersect(r1, 0, 0, 0, r2, .5f, .5f, 45);
		System.out.println("ok4 = "+ok);
	}

    // y = x mod L, where y in [0, L)
    // make sure L>0
    public static float divL(float x, float L) {
        if (x<0)
            x -= ((int)(x/L)) * L - L;
        return x % L;
    }
    // return y = -kL, -(k-1)L, ... -2L, -L, 0, L, 2L, ...
    // where x in [y, y+L)
    public static float board0(float x, float L) {
        final int n = (int)Math.floor(x/L);
        return n*L;
    }
    public static TextureRegion cropX(TextureRegion region, int l, int r) {
        return new TextureRegion(region, l, 0, region.getRegionWidth()-l-r, region.getRegionHeight());
    }
    public static TextureRegion cropX(TextureRegion region, int boarder) {
        return cropX(region, boarder, boarder);
    }
    public static TextureRegion cropY(TextureRegion region, int t, int b) {
        return new TextureRegion(region, 0, t,
                region.getRegionWidth(), region.getRegionHeight()-t-b);
    }
    public static TextureRegion cropY(TextureRegion region, int boarder) {
        return cropY(region, boarder, boarder);
    }
}

