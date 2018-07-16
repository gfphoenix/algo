package com.fphoenix.common.utils;

/**
 * Created by wuhao on 10/19/16.
 */
public class DigitNumber {
    float dest_value;
    float current_value;
    float alpha;

    public DigitNumber(){}
    public DigitNumber(float value, float alpha) {
        this.current_value = this.dest_value = value;
        this.alpha = alpha;
    }
    public void setValue(float value) {
        this.current_value = this.dest_value = value;
    }
    public float getDestValue() {
        return dest_value;
    }
    public void setDestValue(float dest_value) {
        this.dest_value = dest_value;
    }
    public float getCurrentValue() {
        return current_value;
    }
    public boolean equal() {
        return dest_value == current_value;
    }
    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void update(float delta) {
        if (dest_value != current_value) {
            current_value = current_value * (1-alpha) + dest_value * alpha;
        }
    }
}
