package com.tlcsdm.fxrobotstudio.model;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;

/**
 * Types of actions that can be performed in a workflow step.
 */
@XmlEnum
public enum ActionType {

    @XmlEnumValue("mouse_click")
    MOUSE_CLICK,

    @XmlEnumValue("mouse_double_click")
    MOUSE_DOUBLE_CLICK,

    @XmlEnumValue("mouse_right_click")
    MOUSE_RIGHT_CLICK,

    @XmlEnumValue("mouse_move")
    MOUSE_MOVE,

    @XmlEnumValue("mouse_drag")
    MOUSE_DRAG,

    @XmlEnumValue("key_press")
    KEY_PRESS,

    @XmlEnumValue("key_type")
    KEY_TYPE,

    @XmlEnumValue("delay")
    DELAY,

    @XmlEnumValue("image_recognition")
    IMAGE_RECOGNITION,

    @XmlEnumValue("scroll")
    SCROLL

}
