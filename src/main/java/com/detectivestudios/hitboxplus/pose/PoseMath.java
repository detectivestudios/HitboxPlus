/*
 * Hitbox+ by DetectiveStudios
 * Copyright (c) 2026 DetectiveStudios and the respective contributors.
 *
 * Licensed under the DetectiveStudios Source-Available Contribution License.
 * See LICENSE.txt for the complete terms and third-party exceptions.
 *
 * Contribution forks and pull requests to the official GitHub repository are
 * permitted. Reuse in other projects and redistribution require separate written
 * permission, except as expressly provided in the license.
 */

package com.detectivestudios.hitboxplus.pose;

public class PoseMath {

  public static final float PI = (float) Math.PI;
  public static final float HALF_PI = PI * 0.5F;
  public static final float DEG_TO_RAD = PI / 180.0F;

  public static float sin(float value) {
    return (float) Math.sin(value);
  }

  public static float cos(float value) {
    return (float) Math.cos(value);
  }

  public static float lerp(float fraction, float start, float end) {
    return start + fraction * (end - start);
  }

  public static float clamp(float value, float min, float max) {
    return Math.max(min, Math.min(max, value));
  }
}
