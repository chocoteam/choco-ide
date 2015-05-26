package compilation;

class ChocoProjectImpl implements ChocoProject {
    @Override
    public void init(){
        System.out.println("test");
    }
    
    @Override
    public void run(){
        System.out.println("run");
    }
}
