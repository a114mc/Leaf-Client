package net.nonemc.leaf.features.module.modules.combat.aura.data

import net.nonemc.leaf.features.module.modules.combat.Aura

val YawData = listOf(
    -179.97754, -168.77113, -148.31178, -128.98335, -108.11276,
    -89.70953, -56.501587, -25.349823, 6.830078, 33.355316,
    57.413025, 83.01291, 123.62317, 148.91467, 164.0279,
    168.6543, 171.5329, 180.000
)

val YawData2 = if (!Aura.SData) listOf(
    179.87415, 177.20117, 171.03253, 154.17163
    , 137.20795, 128.16052, 121.169495, 111.710815,
    102.04651, 91.55963, 83.12909, 75.31555, 64.520325
    , 53.930542, 44.266113, 30.386597, 19.694214,
    2.9360352, -10.840576, -16.392212, -19.579224,
    -24.719849, -36.23462, -50.73059, -65.432495,
    -79.62024, -94.83618, -115.19263, -130.1001,
    -143.67065, -157.75562, -169.27026, -179.3457
) else listOf(
        -179.3457, -169.27026,177.20117, 179.87415
)