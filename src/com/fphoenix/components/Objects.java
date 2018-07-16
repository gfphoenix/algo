package com.fphoenix.components;

/**
 * Created by alan on 18-3-19.
 */


import com.badlogic.gdx.utils.Pool;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuhao on 3/16/17.
 */
public class Objects {
    static final Pool<ComponentActor> actors = new Pool<ComponentActor>(16, 128) {
        @Override
        protected ComponentActor newObject() {
            return new ComponentActor();
        }
    };
    static Map<Class<? extends Component>, Pool<Component>> map_components;

    static final Pool<SkeletonComponent> skeletonComponents = new Pool<SkeletonComponent>() {
        @Override
        protected SkeletonComponent newObject() {
            return new SkeletonComponent();
        }
    };

    public static ComponentActor getCA() {
        return actors.obtain();
    }

    public static void putCA(ComponentActor actor) {
        if (actor.getClass() == ComponentActor.class)
            actors.free(actor);
    }

    static Map<SkeletonData, Pool<SkeletonComponent>> skeleton_pool = new HashMap<SkeletonData, Pool<SkeletonComponent>>(70);

    private static SkeletonComponent cacheSkeletonComponent(final SkeletonData skeletonData, int cap, int max) {
        Pool<SkeletonComponent> pool = skeleton_pool.get(skeletonData);
        if (pool == null) {
            pool = new Pool<SkeletonComponent>(cap, max) {
                @Override
                protected SkeletonComponent newObject() {
                    SkeletonComponent skeletonComponent = new SkeletonComponent();
                    skeletonComponent.setSk(skeletonData);
                    return skeletonComponent;
                }
            };
            skeleton_pool.put(skeletonData, pool);
        }
        SkeletonComponent sk = pool.obtain();
        sk.reset();
        return sk;
    }

    public static SkeletonComponent skeletonDataToComponent(final SkeletonData skeletonData) {
        Pool<SkeletonComponent> pool = skeleton_pool.get(skeletonData);
        if (pool == null) {
            pool = new Pool<SkeletonComponent>() {
                @Override
                protected SkeletonComponent newObject() {
                    SkeletonComponent skeletonComponent = new SkeletonComponent();
                    skeletonComponent.setSk(skeletonData);
                    return skeletonComponent;
                }
            };
            skeleton_pool.put(skeletonData, pool);
        }
        SkeletonComponent sk = pool.obtain();
        sk.reset();
        return sk;
    }

    private static void putSkeletonComponent(SkeletonComponent skeletonComponent) {
        Skeleton skeleton = skeletonComponent.getSkeleton();
        SkeletonData skeletonData = skeleton.getData();
        Pool<SkeletonComponent> pool = skeleton_pool.get(skeletonData);
        if (pool != null) {
            pool.free(skeletonComponent);
        }
    }

    public static synchronized <T extends Component> T getComponent(Class<T> cls) {
        Pool<? extends Component> pool = map_components.get(cls);
        if (pool == null) return null;
        Component o = pool.obtain();
        o.reset();
        return (T) o;
    }

    public static synchronized void putComponent(Component c) {
//        Class<? extends Component> clazz = c.getClass();
//        if (clazz == SkeletonComponent.class) {
//            putSkeletonComponent(((SkeletonComponent) c));
//        } else {
//            Pool<Component> pool = map_components.get(clazz);
//            if (pool != null)
//                pool.free(c);
//        }
    }

}