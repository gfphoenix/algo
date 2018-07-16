package com.esotericsoftware.spine.ext;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.AttachmentLoader;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.esotericsoftware.spine.attachments.SkinnedMeshAttachment;

/**
 * Created by wuhao on 3/2/17.
 */
public class DummyAttachmentLoader implements AttachmentLoader {
    AttachmentMapper mapper;
    public DummyAttachmentLoader(AttachmentMapper mapper) {
        if (mapper==null) {
            throw new NullPointerException("mapper cannot be null");
        }
        this.mapper = mapper;
    }
    public DummyAttachmentLoader(TextureRegion region) {
        this(new DummyAttachmentMapper(region));
    }
    public DummyAttachmentLoader(TextureAtlas ta) {
        this(new AtlasMap(ta));
    }
    @Override
    public RegionAttachment newRegionAttachment(Skin skin, String name, String path) {
        TextureRegion region = mapper.map(path);
        if (region == null)
            throw new RuntimeException("Region not found in atlas: " + path + " (region attachment: " + name + ")");

        RegionAttachment attachment = new RegionAttachment(name);
        attachment.setRegion(region);
        return attachment;
    }

    @Override
    public MeshAttachment newMeshAttachment(Skin skin, String name, String path) {
        TextureRegion region = mapper.map(path);
        if (region == null) throw new RuntimeException("Region not found in atlas: " + path + " (mesh attachment: " + name + ")");

        MeshAttachment attachment = new MeshAttachment(name);
        attachment.setRegion(region);
        return attachment;
    }

    @Override
    public SkinnedMeshAttachment newSkinnedMeshAttachment(Skin skin, String name, String path) {
        TextureRegion region = mapper.map(path);
        if (region == null)
            throw new RuntimeException("Region not found in atlas: " + path + " (skinned mesh attachment: " + name + ")");

        SkinnedMeshAttachment attachment = new SkinnedMeshAttachment(name);
        attachment.setRegion(region);
        return attachment;
    }

    @Override
    public BoundingBoxAttachment newBoundingBoxAttachment(Skin skin, String name) {
        return new BoundingBoxAttachment(name);
    }
}
