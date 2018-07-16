package com.esotericsoftware.spine.ext;

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
public class DoomyAttachmentLoader implements AttachmentLoader {
    TextureRegion region;
    public DoomyAttachmentLoader(TextureRegion region) {
        if (region==null) {
            throw new NullPointerException("region cannot be null");
        }
        this.region = region;
    }
    @Override
    public RegionAttachment newRegionAttachment(Skin skin, String name, String path) {
        RegionAttachment attachment = new RegionAttachment(name);
        attachment.setRegion(region);
        return attachment;
    }

    @Override
    public MeshAttachment newMeshAttachment(Skin skin, String name, String path) {
        MeshAttachment attachment = new MeshAttachment(name);
        attachment.setRegion(region);
        return attachment;
    }

    @Override
    public SkinnedMeshAttachment newSkinnedMeshAttachment(Skin skin, String name, String path) {
        SkinnedMeshAttachment attachment = new SkinnedMeshAttachment(name);
        attachment.setRegion(region);
        return attachment;
    }

    @Override
    public BoundingBoxAttachment newBoundingBoxAttachment(Skin skin, String name) {
        return new BoundingBoxAttachment(name);
    }
}
