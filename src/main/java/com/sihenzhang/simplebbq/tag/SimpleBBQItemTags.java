package com.sihenzhang.simplebbq.tag;

import com.sihenzhang.simplebbq.util.TagUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class SimpleBBQItemTags {
    public static final TagKey<Item> SKEWER = TagUtils.createItemTag("skewer");

    public static final TagKey<Item> CAN_BE_SEASONED_BY_HONEY = TagUtils.createItemTag("can_be_seasoned/honey");
    public static final TagKey<Item> CAN_BE_SEASONED_BY_CHILI_POWDER = TagUtils.createItemTag("can_be_seasoned/chili_powder");
    public static final TagKey<Item> CAN_BE_SEASONED_BY_CUMIN = TagUtils.createItemTag("can_be_seasoned/cumin");
    public static final TagKey<Item> CAN_BE_SEASONED_BY_SALT_AND_PEPPER = TagUtils.createItemTag("can_be_seasoned/salt_and_pepper");
}
