package tfar.booktoclipboard;

import com.mojang.brigadier.CommandDispatcher;
import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;

import java.util.List;

public class ExampleMod implements ClientCommandPlugin {

	public static final String MODID = "booktoclipboard";

	@Override
	public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher) {
		dispatcher.register(ArgumentBuilders.literal(MODID).executes(
						source -> {
							PlayerEntity player = MinecraftClient.getInstance().player;
							ItemStack stack = player.getMainHandStack();
							if (stack.getItem() == Items.WRITTEN_BOOK) {
								CompoundTag nbt = stack.getTag();
								StringBuilder sb = new StringBuilder();
								List<String> pages = BookScreen.readPages(nbt);
								for (int i = 0 ; i < pages.size();i++) {
									sb.append("-----Page "+i+"-----");
									sb.append(System.getProperty("line.separator"));
									StringRenderable stringRenderable = Text.Serializer.fromJson(pages.get(i));
									String[] split = stringRenderable.getString().split(System.getProperty("line.separator"));
									for (String s : split) {
										sb.append(s);
										sb.append(System.getProperty("line.separator"));
									}
								}
								player.sendMessage(new LiteralText("Copied book contents to clipboard"),false);
								MinecraftClient.getInstance().keyboard.setClipboard(sb.toString());
							}
							return 1;
						}
		));
	}
}
