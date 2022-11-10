/*
 * Copyright (c) 2020-2022 Dirt Powered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.dirtpowered.dirtmv.network.versions.Beta7to6;

import java.util.Map.Entry;
import java.util.function.Consumer;

import com.github.dirtpowered.dirtmv.data.MinecraftVersion;
import com.github.dirtpowered.dirtmv.data.protocol.PacketData;
import com.github.dirtpowered.dirtmv.data.protocol.Type;
import com.github.dirtpowered.dirtmv.data.protocol.TypeHolder;
import com.github.dirtpowered.dirtmv.data.protocol.objects.ItemStack;
import com.github.dirtpowered.dirtmv.data.translator.PacketDirection;
import com.github.dirtpowered.dirtmv.data.translator.PacketTranslator;
import com.github.dirtpowered.dirtmv.data.translator.ServerProtocol;
import com.github.dirtpowered.dirtmv.data.utils.PacketUtil;
import com.github.dirtpowered.dirtmv.network.server.ServerSession;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;

public class ProtocolBeta7To6 extends ServerProtocol {
	static int i = -1;

    public ProtocolBeta7To6() {
        super(MinecraftVersion.B1_1, MinecraftVersion.A1_2_6);
    }

    @Override
    public void registerTranslators() {
        // login
        addTranslator(0x01, PacketDirection.TO_SERVER, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return PacketUtil.createPacket(0x01, new TypeHolder[]{
                        set(Type.INT, 6),
                        data.read(1),
                        data.read(2),
                        data.read(3),
                        data.read(4),
                });
            }
        });
        
        // window items
        addTranslator(0x05, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {
            	
            	return cancel();
            	/*Integer type = data.read(Type.INT, 0);
            	ItemStack[] items = data.read(Type.V1_3B_ITEM_ARRAY, 1);
            	
            	// Convert separated player inventory sections to beta's combined inventory
            	int add;
            	switch(type) {
            		case -3: // Crafting
            			add = 0;
            			break;
            		case -2: // Armor
            			add = 5;
            			break;
            		default: // Main inventory section
            			add = 9;
            	}
            	
            	for (ItemStack item: items) {
            		if (item != null) item.setAmount(item.getAmount() + add);
            	}

                return PacketUtil.createPacket(0x68, new TypeHolder[]{
                		set(Type.INT, 0),
                		set(Type.V1_1B_ITEM_ARRAY, items)
                });*/
            }
        });
        
        // health update
        addTranslator(0x08, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {
            	
            	Integer health = data.read(Type.BYTE, 0) & 0xFFFF;

                return PacketUtil.createPacket(0x08, new TypeHolder[]{
                        set(Type.SHORT, health.shortValue())
                });
            }
        });
        
        // holding change
        addTranslator(0x10, PacketDirection.TO_SERVER, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return PacketUtil.createPacket(0x10, new TypeHolder[]{
                        set(Type.INT, 0),
                        data.read(1)
                });
            }
        });
        
        // holding change
        addTranslator(0x10, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return PacketUtil.createPacket(0x10, new TypeHolder[]{
                        data.read(1)
                });
            }
        });
        
        // window close
        addTranslator(0x65, PacketDirection.TO_SERVER, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return cancel();
            }
        });
        
        // window click
        addTranslator(0x66, PacketDirection.TO_SERVER, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return cancel();
            }
        });
        
        // inventory add, TODO: Translate this
        addTranslator(0x11, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

                return cancel();
            }
        });
        
        // complex entities, TODO: Translate this
        addTranslator(0x3B, PacketDirection.TO_CLIENT, new PacketTranslator() {

            @Override
            public PacketData translate(ServerSession session, PacketData data) {

            	/*CompoundBinaryTag a = data.read(Type.COMPOUND_TAG, 3);
            	
            	if (a != null) {
            		i++;
            	
            		System.out.println("(" + i + ") Trying to spawn complex entity:");
	            	a.forEach(new Consumer<Entry<String, ? extends BinaryTag>>() {
	
						@Override
						public void accept(Entry<String, ? extends BinaryTag> t) {
							if (t.getValue().type().id() == 8) {
								System.out.println("(" + i + ") 	" + t.getKey() + ": " + ((StringBinaryTag) t.getValue()).value());
							}
						}
	            		
	            	});
	            	System.out.println("(" + i + ") End of NBT");
            	}*/
            	
                return cancel();
            }
        });
    }
}
