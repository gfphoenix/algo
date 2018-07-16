package com.fphoenix.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;

/**
 * Created by alan on 18-5-18.
 */

public class ParticleComponent extends RenderComponent {
    TextureRegion []region_set;
    float life1, life2;
    // angle is any
    float offsetX, offsetY;
    float speed, rot_speed;
    Array<Particle> particles = new Array<Particle>();
    Action action;
    float elapsed;
    float emit_time;
    Color from = Color.RED;
    Color to = Color.WHITE;
    Pool pool = new Pool();


    class Particle {
        float x, y;
        float speedX, speedY;
        float life, age;
        float rot, rot_speed;
        float cc;
        TextureRegion region;
        Color color = new Color();
        Pool pool;


        boolean update(float dt) {
            age += dt;
            float p = age/life;
//            cc = Color.mix(null, from, to, p);
            x += speedX * dt;
            y += speedY * dt;
            rot += rot_speed * dt;
            if (age>life) {
                if (pool!=null)
                    pool.free(this);
                return true;
            }
            return false;
        }
        void draw(SpriteBatch batch, float xx, float yy) {
            final float w = region.getRegionWidth();
            final float h = region.getRegionHeight();
            batch.setColor(cc);
            batch.draw(region, x-w/2, y-h/2, w/2,h/2, w,h, 1,1, rot);
        }
    }

    class Pool extends com.badlogic.gdx.utils.Pool<Particle> {
        @Override
        protected Particle newObject() {
            return new Particle();
        }
    }
    public ParticleComponent setRegions(TextureRegion []set) {
        this.region_set = set;
        return this;
    }

    public ParticleComponent setLife(float life1, float life2) {
        this.life1 = life1;
        this.life2 = life2;
        return this;
    }
    public ParticleComponent setOffset(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return this;
    }

    public ParticleComponent setSpeed(float linear_speed, float angle_speed) {
        this.speed = linear_speed;
        this.rot_speed = angle_speed;
        return this;
    }

    public ParticleComponent setEmitTime(float emit_time) {
        this.emit_time = emit_time;
        return this;
    }

    float random11() {
        return (float)(Math.random()*2-1);
    }

    float random1() {
        return (float)Math.random();
    }
    int random_n(int n) {
        return (int) (Math.random() * n);
    }
    Color randomColor(Color c) {
        c.r = MathUtils.random(.7f, 1f);
        c.g = MathUtils.random(.7f, 1f);
        c.b = MathUtils.random(.7f, 1f);
        c.a = MathUtils.random(.4f, 1f);
        return c;
    }
    Particle create(ComponentActor ca) {
        Particle particle = pool.obtain();
        float dx = random11();
        float dy = random11();
        particle.x = ca.getX() + dx * offsetX;
        particle.y = ca.getY() + dy * offsetY;
        particle.speedX = speed * dx;
        particle.speedY = speed * dy;
        particle.life = life1 + (life2-life1)*random1();
        particle.age = 0;
//        particle.cc = Color.mix(null, from, to, (float) (Math.random() * .8 + .2));
        particle.cc = randomColor(Color.tmp).toFloatBits();
        particle.region = region_set[random_n(region_set.length)];
        particle.rot_speed = rot_speed * random11();
        particle.pool = pool;
        return particle;
    }

    @Override
    public void drawC(SpriteBatch batch, float parentAlpha) {
        batch.setColor(Color.RED);
        ComponentActor a = getActor();
        for (int i=particles.size-1; i>=0; i--) {
            particles.get(i).draw(batch, a.getX(), a.getY());
        }
    }
    void update_particles(float dt) {
        for (int i=particles.size-1; i>=0; i--) {
            Particle particle = particles.get(i);
            if (particle.update(dt)) {
                particles.removeIndex(i);
            }
        }
    }
    void update_emitter(float dt) {
        elapsed += dt;
        while (elapsed >= emit_time) {
            elapsed -= emit_time;
            emit();
        }
    }
    public void stop() {
        if (action!=null) {
            ComponentActor actor = getActor();
            if (actor!=null)
                actor.removeAction(action);
        }
    }
    public void start() {
        ComponentActor actor = getActor();
        if (action==null) {
            action = new Action() {
                @Override
                public boolean act(float dt) {
                    update_particles(dt);
                    update_emitter(dt);
                    return false;
                }
            };
        }
        if (actor!=null)
            actor.addAction(action);
    }

    void emit() {
        Particle particle = create(getActor());
        particles.add(particle);
    }
    @Override
    public void enter() {
        super.enter();
        ComponentActor actor = getActor();
        if (action!=null) actor.removeAction(action);
        action = new Action() {
            @Override
            public boolean act(float dt) {
                update_particles(dt);
                update_emitter(dt);
                return false;
            }
        };
        actor.addAction(action);
    }

    @Override
    public void exit() {
        if (action!=null) {
            getActor().removeAction(action);
            action=null;
        }
        super.exit();
    }
}
