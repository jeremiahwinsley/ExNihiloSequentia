package novamachina.exnihilosequentia.common.crafting.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import novamachina.exnihilosequentia.common.crafting.ExNihiloRecipeSerializer;
import novamachina.exnihilosequentia.common.crafting.sieve.MeshWithChance;
import novamachina.exnihilosequentia.common.crafting.sieve.SieveRecipe;
import novamachina.exnihilosequentia.common.init.ExNihiloBlocks;

public class SieveRecipeSerializer extends ExNihiloRecipeSerializer<SieveRecipe> {

  @Override
  public ItemStack getIcon() {
    return new ItemStack(ExNihiloBlocks.SIEVE_OAK.get());
  }

  @Override
  public SieveRecipe fromNetwork(@Nonnull final ResourceLocation recipeId,
      @Nonnull final FriendlyByteBuf buffer) {
    @Nonnull final Ingredient input = Ingredient.fromNetwork(buffer);
    @Nonnull final ItemStack drop = buffer.readItem();
    @Nonnull final List<MeshWithChance> rolls = new ArrayList<>();
    final int count = buffer.readInt();
    for (int i = 0; i < count; i++) {
      rolls.add(MeshWithChance.read(buffer));
    }
    final boolean isWaterlogged = buffer.readBoolean();
    return new SieveRecipe(recipeId, input, drop, rolls, isWaterlogged);
  }

  @Override
  public void toNetwork(@Nonnull final FriendlyByteBuf buffer, @Nonnull final SieveRecipe recipe) {
    recipe.getInput().toNetwork(buffer);
    buffer.writeItem(recipe.getDrop());
    buffer.writeInt(recipe.getRolls().size());
    for (@Nonnull final MeshWithChance meshWithChance : recipe.getRolls()) {
      meshWithChance.write(buffer);
    }
    buffer.writeBoolean(recipe.isWaterlogged());
  }

  @Override
  protected SieveRecipe readFromJson(@Nonnull final ResourceLocation recipeId,
      @Nonnull final JsonObject json) {
    @Nonnull final Ingredient input = Ingredient.fromJson(json.get("input"));
    @Nonnull final ItemStack drop = readOutput(json.get("result"));
    @Nonnull final List<MeshWithChance> rolls = new ArrayList<>();
    for (@Nonnull final JsonElement element : json.get("rolls").getAsJsonArray()) {
      rolls.add(MeshWithChance.deserialize(element));
    }
    if (json.has("waterlogged")) {
      return new SieveRecipe(recipeId, input, drop, rolls, json.get("waterlogged").getAsBoolean());
    }
    return new SieveRecipe(recipeId, input, drop, rolls, false);
  }
}
