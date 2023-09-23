export default function Home() {
  const leaderboardData = [
    { name: 'Player1', score: 150 },
    { name: 'Player2', score: 120 },
    { name: 'Player3', score: 100 },
    // ... more players
  ];

  const tenStackData = [
    { stackName: 'Stack1', blocks: 10 },
    { stackName: 'Stack2', blocks: 8 },
    { stackName: 'Stack3', blocks: 5 },
    // ... more stacks
  ];

  return (
    <section>
      <h1 className="text-4xl">Sprock</h1>
      <img src="https://mc-heads.net/avatar/sprock/100"></img>
      <table className="table-auto">
        <tbody>
          <tr>
            <td>Kills</td>
            <td>1961</td>
          </tr>
          <tr>
            <td>Witchy Woman</td>
            <td>1972</td>
          </tr>
          <tr>
            <td>Shining Star</td>
            <td>1975</td>
          </tr>
        </tbody>
      </table>
    </section>
  );
}
