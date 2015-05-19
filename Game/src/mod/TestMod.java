package mod;

public class TestMod extends zip.BaseMod {

	@Override
	public void onLevelStarts() {
		System.out.println("on level starts...");
	}

	@Override
	public void postCreateGameObjects() {
		System.out.println("post create game objects...");
	}

	@Override
	public void preCreateGameObjects() {
		System.out.println("pre create game objects...");
	}

	@Override
	public void onGameStarts() {
		System.out.println("on Game Starts...");
	}

	@Override
	protected void onLoad() {
		new Test().showMSG(game.getMenu());
		System.out.println("on Load...");
	}

	@Override
	protected void onUnload() {
		System.out.println("on Unload...");
	}

}
