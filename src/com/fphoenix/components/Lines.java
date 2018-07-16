//package com.fphoenix.components;
//
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//
///**
// * Created by wuhao on 8/11/16.
// */
//public class Lines extends RenderComponent {
//    ShapeRenderer shapeRenderer;
//    PointSource pointSource;
//
//    public ShapeRenderer getShapeRenderer() {
//        return shapeRenderer;
//    }
//
//    public void setShapeRenderer(ShapeRenderer shapeRenderer) {
//        this.shapeRenderer = shapeRenderer;
//    }
//
//    public PointSource getPointSource() {
//        return pointSource;
//    }
//
//    public void setPointSource(PointSource pointSource) {
//        this.pointSource = pointSource;
//    }
//
//    @Override
//    public void drawC(SpriteBatch batch, float parentAlpha) {
//        ComponentActor actor = getActor();
//        int n = pointSource==null ? 0 : pointSource.count();
//        if (n<2) return;
//        batch.end();
//        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
//        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(actor.getColor());
//        for (int i=0; i<n-1; i+=2) {
//            shapeRenderer.line(pointSource.xAt(i), pointSource.yAt(i),
//                    pointSource.xAt(i+1), pointSource.yAt(i+1));
//        }
//        shapeRenderer.end();
//        batch.begin();
//    }
//}
