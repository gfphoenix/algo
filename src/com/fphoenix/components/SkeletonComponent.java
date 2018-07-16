package com.fphoenix.components;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;

/**
 * Created by wuhao on 8/11/16.
 */
public class SkeletonComponent extends RenderComponent implements PolyGroup.PolyDrawable{
    private static final SkeletonRenderer renderer_ = new SkeletonRenderer();
    private boolean premultipliedAlpha = false;
    PolygonSpriteBatch polygonSpriteBatch;
    SkeletonRenderer renderer = renderer_;
    private Skeleton skeleton;
    private AnimationState state;
    NameMapper nameMapper = NameMapper.I;
    final BaseBehavior updater = new BaseBehavior() {
        @Override
        public boolean update(float dt) {
            state.update(dt);
            state.apply(skeleton);
            return false;
        }
    };
    Hook hook;

    public interface Hook {
        void update(Skeleton skeleton);
    }
    public SkeletonComponent setSk(Skeleton sk) {
        this.skeleton = sk;
        return this;
    }
    public SkeletonComponent setSk(SkeletonData sk) {
        this.skeleton = new Skeleton(sk);
        return this;
    }
    public SkeletonComponent setNp(NameMapper mapper) {
        this.nameMapper = mapper == null ? NameMapper.I : mapper;
        return this;
    }
    public SkeletonComponent setRenderer(SkeletonRenderer renderer) {
        this.renderer = renderer!=null ? renderer : renderer_;
        return this;
    }
    public SkeletonComponent setBatch(PolygonSpriteBatch batch) {
        this.polygonSpriteBatch = batch;
        return this;
    }
    public SkeletonComponent setAni(AnimationStateData stateData) {
        this.state = new AnimationState(stateData);
        return this;
    }
    public SkeletonComponent setAni(AnimationState animationState) {
        this.state = animationState;
        return this;
    }

    @Override
    public void reset() {
        super.reset();
        premultipliedAlpha = false;
        polygonSpriteBatch = null;
        renderer = renderer_;
        state.clearTracks();
        state.clearListeners();
        skeleton.setSkin(skeleton.getData().getDefaultSkin());
        skeleton.setFlipX(false);
        skeleton.setFlipY(false);

        state.getHook().clear();
        hook = null;
        skeleton.setToSetupPose();
    }

    public boolean isPremultipliedAlpha() {
        return premultipliedAlpha;
    }
    public void setPremultipliedAlpha(boolean premultipliedAlpha) {
        this.premultipliedAlpha = premultipliedAlpha;
    }
    public Skeleton getSkeleton() {
        return skeleton;
    }
    public SkeletonRenderer getRenderer() {
        return renderer;
    }
    public PolygonSpriteBatch getPolygonSpriteBatch() {
        return polygonSpriteBatch;
    }
    public NameMapper getNameMapper() {return nameMapper;}
    public AnimationState.TrackEntry setAnimation(int trackIndex, String animation, boolean loop) {
        return state.setAnimation(trackIndex, nameMapper.s(animation), loop);
    }
    public AnimationState.TrackEntry addAnimation(int trackIndex, String animation, boolean loop, float delay) {
        return state.addAnimation(trackIndex, nameMapper.s(animation), loop, delay);
    }
    public void addListener(AnimationState.AnimationStateListener listener) {
        state.addListener(listener);
    }
    public void removeListener(AnimationState.AnimationStateListener listener) {
        state.removeListener(listener);
    }
    public AnimationState.TrackEntry getCurrent(int trackIndex) {
        return state.getCurrent(trackIndex);
    }
    public void setMix(String from, String to, float duration) {
        state.getData().setMix(nameMapper.s(from), nameMapper.s(to), duration);
    }
    @Override
    public void enter() {
        super.enter();
        if (state==null)
            state = new AnimationState(new AnimationStateData(skeleton.getData()));
        skeleton.setToSetupPose();
        getActor().addBehavior(updater);
    }

    @Override
    public void exit() {
        getActor().removeBehavior(updater);
        super.exit();
    }

    public Hook getHook() {
        return hook;
    }

    public void setHook(Hook hook) {
        this.hook = hook;
    }

    @Override
    public void drawC(SpriteBatch batch, float parentAlpha) {
        if (skeleton==null || polygonSpriteBatch==null) return;
//        debugDraw(batch, parentAlpha);
        batch.end();
        ComponentActor actor = getActor();
        skeleton.setPosition(actor.getX(), actor.getY());
        Bone root = skeleton.getRootBone();
        float scaleX = root.getScaleX();
        float scaleY = root.getScaleY();
        root.setScale(actor.getScaleX(), actor.getScaleY());
        if (hook!=null) hook.update(skeleton);
        skeleton.setColor(actor.getColor());
        skeleton.updateWorldTransform();
        polygonSpriteBatch.setProjectionMatrix(batch.getProjectionMatrix());
        polygonSpriteBatch.setTransformMatrix(batch.getTransformMatrix());
        polygonSpriteBatch.begin();
        polygonSpriteBatch.setColor(actor.getColor());
        renderer.setPremultipliedAlpha(this.premultipliedAlpha);
        renderer.draw(polygonSpriteBatch, skeleton);
        polygonSpriteBatch.end();
        root.setScale(scaleX, scaleY);
        batch.begin();
    }
    public void drawPoly(PolygonSpriteBatch batch, SkeletonRenderer renderer) {
        if (skeleton==null) return;
//        debugDraw(batch);
        ComponentActor actor = getActor();
        skeleton.setPosition(actor.getX(), actor.getY());
        Bone root = skeleton.getRootBone();
        float scaleX = root.getScaleX();
        float scaleY = root.getScaleY();
        root.setScale(actor.getScaleX(), actor.getScaleY());
        if (hook!=null) hook.update(skeleton);
        skeleton.updateWorldTransform();
        batch.setColor(actor.getColor());
        renderer.setPremultipliedAlpha(this.premultipliedAlpha);
        renderer.draw(batch, skeleton);
        root.setScale(scaleX, scaleY);
    }
}
